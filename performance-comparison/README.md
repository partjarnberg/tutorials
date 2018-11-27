# Performance comparison
For curiosity I wanted to compare Spring WebFlux and Undertow on how the perform when implementing a REST API.

__DISCLAIMER!__ I have little (if any) experience of Spring WebFlux and how it can be configured. Feel free to point out if I've missed something obvious.
Also I ran all the tests on my laptop acting both client and server. 

Laptop specs:
```
macOS High Sierra version 10.13.6
MacBook Pro 13-inch, 2016
Processor 3,3 GHz Intel Core i7
Memory 16GB 2133 MHz LPDDR3
```
Checking limit of file descriptors
```
➜  ~ launchctl limit maxfiles
	maxfiles    256            unlimited
```

## Summary
I did the simplest possible setup I could think of firing up Spring WebFlux and Undertow separately with the following requirements: 
* Create HTTP handler/controller
* Dispatch from I/O thread 
* Create and serve JSON starting with a POJO
* Run simple load test using [wrk](https://github.com/wg/wrk) 

![Histogram comparison][histogram-comparison]

__Spring WebFlux (default)__ uses Netty as web server and could handle __922 623__ requests in __30.10s__ (30 649.32 requests/s)

__Spring WebFlux - Undertow__ could handle __922 623__ requests in __30.05s__ (35 069.13 requests/s)

__Undertow dispatching from I/O thread__ could handle __2 060 165__ requests in __30.08s__ (68 490.82 requests/s)

## Build
Build both Spring WebFlux and Undertow projects from root by running the following.
```
./gradlew clean build bootJar shadowJar
```
## Spring boot - webflux
### Run
```
java -jar spring-webflux/build/libs/spring-webflux-1.0-SNAPSHOT.jar
```
### Out-of-box
```java
package com.skillsdevelopment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public RouterFunction<ServerResponse> routes() {
        final Car jaguar = new Car("Jaguar", "E-type",
                2, 2, 1234);
        return RouterFunctions
                .route(RequestPredicates.GET("/")
                        .and(RequestPredicates.accept(APPLICATION_JSON)), request ->
                        ServerResponse.ok().contentType(APPLICATION_JSON)
                        .body(BodyInserters.fromObject(jaguar)));
    }
}
```
```
➜  ~ wrk -t8 -c500 -d30s http://localhost:8080
Running 30s test @ http://localhost:8080
  8 threads and 500 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    23.45ms   78.02ms   1.61s    98.55%
    Req/Sec     3.89k   770.84    11.13k    89.62%
  922623 requests in 30.10s, 140.78MB read
Requests/sec:  30649.32
Transfer/sec:      4.68MB
```
### Using undertow as web server
By configuring build.gradle we can select _Undertow_ as web server instead of _Netty_ (default).
```
...

configurations {
    // exclude Reactor Netty
    implementation {
        module: 'spring-boot-starter-reactor-netty'
    }
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-webflux:2.1.0.RELEASE'
    implementation 'org.springframework.boot:spring-boot-starter-undertow:2.1.0.RELEASE'
}

...
```
```
➜  ~ wrk -t8 -c500 -d30s http://localhost:8080
Running 30s test @ http://localhost:8080
  8 threads and 500 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    15.55ms   12.28ms 179.00ms   91.96%
    Req/Sec     4.41k     0.92k    5.64k    84.58%
  1053997 requests in 30.05s, 222.14MB read
Requests/sec:  35069.13
Transfer/sec:      7.39MB
```
## Undertow
### Run
```
java -jar undertow/build/libs/undertow-1.0-SNAPSHOT-all.jar
```
### Out-of-box
```java
package com.skillsdevelopment;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.undertow.Undertow;
import io.undertow.server.RoutingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

import static io.undertow.UndertowOptions.ENABLE_HTTP2;
import static io.undertow.util.Headers.CONTENT_TYPE;
import static io.undertow.util.StatusCodes.OK;

public class Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        final Car jaguar = new Car("Jaguar", "E-type",
                2, 2, 1234);
        final ObjectMapper objectMapper = new ObjectMapper();

        final Undertow server = Undertow.builder()
                .setServerOption(ENABLE_HTTP2, true)
                .addHttpListener(8080, "0.0.0.0", new RoutingHandler()
                        .get("/", dispatcher -> dispatcher.dispatch(exchange -> {
                            exchange.setStatusCode(OK);
                            exchange.getResponseHeaders().put(CONTENT_TYPE, "application/json;charset=utf-8");
                            exchange.getResponseSender().send(ByteBuffer.wrap(objectMapper.writeValueAsBytes(jaguar)));
                        })))
                .build();
        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("Stopping HTTP server...");
            server.stop();
        }));
    }
}
```
```
➜  ~ wrk -t8 -c500 -d30s http://localhost:8080
Running 30s test @ http://localhost:8080
  8 threads and 500 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     7.68ms    7.48ms 176.06ms   95.25%
    Req/Sec     8.67k     2.48k   48.61k    87.84%
  2060165 requests in 30.08s, 461.71MB read
Requests/sec:  68490.82
Transfer/sec:     15.35MB
```

[histogram-comparison]: https://github.com/partjarnberg/tutorials/blob/screenshots/performance-comparison/histogram.png?raw=true "Histogram comparison"