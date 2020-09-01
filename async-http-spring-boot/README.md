# Tutorial - Non-blocking Spring @RestController and http-client 
This is a tutorial written in a blog post format with the purpose of elaborating on non-blocking REST services as well as non-blocking http-clients in Java and Spring in particular.

---

**UPDATE!** _Since this tutorial was written a new non-blocking [HTTP Client](https://docs.oracle.com/en/java/javase/14/docs/api/java.net.http/java/net/http/HttpClient.html) has been introduced since Java 11. Also Spring have created [WebFlux](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html) with its [WebClient](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html#webflux-client) targeted to a reactive programming model. However the foundation is still the same and it is still important as programmer to understand the basic concepts and boundaries of asynchronous programming._

---

## Table of Contents
* [1. Background](#background)
  * [1.1 Time consuming task](#time-consuming-task)
  * [1.2 Blocking controller](#blocking-controller)
    * [1.2.1 Request lifecycle](#request-lifecycle-blocking-controller)
* [2. Spring support for NIO and Servlet 3.x specification](#spring-support-nio-servlet-3)
  * [2.1 Non-blocking controller](#non-blocking-controller)
    * [2.1.1 Callable](#callable)
    * [2.1.2 DeferredResult](#deferred-result")
    * [2.1.3 CompletableFuture](#completable-future)
    * [2.1.4 Request lifecycle](#request-lifecycle-nonblocking-controller-blocking-service)
* [3. Resource client](#resource-client)
  * [3.1 Blocking vs. Non-blocking resource client](#blocking-vs-non-blocking-resource)
  * [3.2 Non-blocking http-client](#non-blocking-http-client)
    * [3.2.1 AsyncRestTemplate](#async-rest-template)
    * [3.2.2 Unirest for Java](#unirest-for-java)
* [4. Combine non-blocking RestController with http-client](#combine-non-blockling-restcontroller-with-http-client)
  * [4.1 Request lifecycle](#request-lifecycle-nonblocking-controller-nonblocking-service)
* [5. Summary and further readings](#summary-further-readings)

## <a name="background"></a>1. Background
This project aims on describing the benefits of *asynchronous* programming when developing a REST API. Both server and client side.
We will elaborate on when support for asynchronous programming was introduced in Java Servlet specifications.

The examples given will be using __Java 8__ and __Spring Framework__ for server implementation. Therefor details on when various features 
in Servlet specification was supported by Spring will be given. The asynchronous http client example is being done using Unirest for Java.
The reason for this is the somewhat small footprint easy to use the Apache HTTP client using this wrapper library. Furthermore, Unirest 
provides a callback mechanism on asynchronous calls making it relatively easy to convert to CallableFutures.

### <a name="time-consuming-task"></a>1.1 Time consuming task
Let's start by laying out a scenario.
1. There's this Resource. Damn it's slow!
2. We want to build REST API consuming the Resource, transform the content somehow and expose another REST API in turn.
    1. Simplest solution first --> Spring @RestController etc. What does that actually give us in terms of performance and scalability?
    2. Can we do it better? We'll give it try!

First of all. We need a really bad performing Resource. It is supposed to be located externally. Let's pretend it is. Use our imagination. 
``` java
@Service
public class ExternalResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalResource.class);

    public String slowHelloWorld(final String input) {
        try {
            LOGGER.debug("Do long running task...");
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        LOGGER.debug("...long running task done!");
        return "Hello " + input;
    }
}
```

Also we need an API client that consumes the bad performing external resource. In reality this could be accessing a file system or a web service etc.

``` java
@Service
public class ApiClient {
    @Autowired
    private ExternalResource resource;

    public String apiCall(final String input) {
        return resource.slowHelloWorld(input);
    }
}
```

### <a name="blocking-controller"></a>1.2 Blocking controller
The normal way of creating a REST endpoint in Spring causes the container thread to be blocked for the entire time of the request.
``` java
@RestController
@RequestMapping("blocking")
public class BlockingController {
    @Autowired
    private ApiClient apiClient;

    @GetMapping(path = "{input}")
    public String get(@PathVariable final String input) {
        return apiClient.apiCall(input);
    }
}
``` 

Logs from requesting the endpoint:
```
2017-08-23 15:14:55.990 [XNIO-2 I/O-2] DEBUG io.undertow.request - Matched prefix path /tutorial-async for path /tutorial-async/blocking/test
2017-08-23 15:14:56.074 [XNIO-2 task-1] DEBUG s.c.api.example.blocking.BlockingController - Using default REST controller endpoint
2017-08-23 15:14:56.075 [XNIO-2 task-1] DEBUG s.c.api.example.service.ApiClient - Blocking API call
2017-08-23 15:14:56.076 [XNIO-2 task-1] DEBUG s.c.a.e.resource.ExternalResource - Do long running task...
2017-08-23 15:14:57.079 [XNIO-2 task-1] DEBUG s.c.a.e.resource.ExternalResource - ...long running task done!
```
Above log entries shows that the HTTP request hits Undertow I/O thread which dispatch the workload to another thread which executes the 
Spring Rest Controller (BlockingController). The BlockingController in turn performs a call to ApiClient which requests the external resource. 
All happens in the same thread. It's blocking. 

#### <a name="request-lifecycle-blocking-controller"></a>1.2.1 Request lifecycle
Lets take a look at the request lifecycle when using a blocking controller.

![Request lifecycle - blocking controller][request-lifecycle-blocking-controller]

## <a name="spring-support-nio-servlet-3"></a>2. Spring support for NIO and Servlet 3.x specification
In 2009 the [Servlet 3.0 specification](https://jcp.org/en/jsr/detail?id=315) was released. One part of this specification is the standardization of how to perform non-blocking processing against underlying web servers. A non-blocking application can be deployed on any web server that supports the Servlet 3.0 specification.
With Spring Boot and its embedded web servers this means Tomcat, Undertow, Jetty etc.

### <a name="non-blocking-controller"></a>2.1 Non-blocking controller
From Spring Framework 3.2 in 2013 support was introduced for developing non-blocking REST services.
It can be done in various ways and which solutions one chooses depends partly on if you want to control the execution context or not.
Lets take a closer look at the different variants.

#### <a name="callable"></a>2.1.1 [Callable](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/Callable.html)
In Spring you can make any existing controller method asynchronous by changing it to return a Callable.
A Callable will then be executed concurrently on behalf of the application.
``` java
@RestController
@RequestMapping("nonblocking")
public class NonBlockingController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NonBlockingController.class);

    @Autowired
    private ApiClient apiClient;

    @RequestMapping("callable/{input}")
    public Callable<String> callable(@PathVariable final String input) {
        LOGGER.debug("Using Callable");
        return () -> apiClient.apiCall(input);
    }
}
``` 

Logs from requesting the endpoint:
```
2017-08-23 15:16:21.228 [XNIO-2 I/O-2] DEBUG io.undertow.request - Matched prefix path /tutorial-async for path /tutorial-async/nonblocking/callable/test
2017-08-23 15:16:21.239 [XNIO-2 task-2] DEBUG s.c.a.e.n.NonBlockingController - Using Callable
2017-08-23 15:16:21.248 [MvcAsync1] DEBUG s.c.api.example.service.ApiClient - Blocking API call
2017-08-23 15:16:21.248 [MvcAsync1] DEBUG s.c.a.e.resource.ExternalResource - Do long running task...
2017-08-23 15:16:22.253 [MvcAsync1] DEBUG s.c.a.e.resource.ExternalResource - ...long running task done!
```

#### <a name="deferred-result"></a>2.1.2 [DeferredResult](https://docs.spring.io/spring/docs/3.2.0.RELEASE/javadoc-api/org/springframework/web/context/request/async/DeferredResult.html)
DeferredResult provides an alternative to using a Callable for asynchronous request processing. While a Callable is executed concurrently on behalf of the application, with a DeferredResult the application can produce the result from a thread of its choice.

DeferredResult was first introduced in Spring Framework 3.2 in 2013. 
``` java
@RestController
@RequestMapping("nonblocking")
public class NonBlockingController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NonBlockingController.class);

    @Autowired
    private ApiClient apiClient;

    @Autowired
    private TaskExecutor taskExecutor;

    @RequestMapping("deferredresult/{input}")
    public DeferredResult<String> deferredResult(@PathVariable final String input) {
        LOGGER.debug("Using DeferredResult");
        final DeferredResult<String> deferredResult = new DeferredResult<>();
        taskExecutor.execute(() -> deferredResult.setResult(apiClient.apiCall(input)));
        return deferredResult;
    }
}
``` 

Logs from requesting the endpoint:
```
2017-08-23 15:18:01.968 [XNIO-2 I/O-2] DEBUG io.undertow.request - Matched prefix path /tutorial-async for path /tutorial-async/nonblocking/deferredresult/test
2017-08-23 15:18:01.974 [XNIO-2 task-4] DEBUG s.c.a.e.n.NonBlockingController - Using DeferredResult
2017-08-23 15:18:01.976 [SimpleAsyncTaskExecutor-1] DEBUG s.c.api.example.service.ApiClient - Blocking API call
2017-08-23 15:18:01.976 [SimpleAsyncTaskExecutor-1] DEBUG s.c.a.e.resource.ExternalResource - Do long running task...
2017-08-23 15:18:02.979 [SimpleAsyncTaskExecutor-1] DEBUG s.c.a.e.resource.ExternalResource - ...long running task done!
```

#### <a name="completable-future"></a>2.1.3 [CompletableFuture](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html)
Since Spring Framework 4.2 a @RestController method can have a return type of java.util.concurrent.CompletableFuture for asynchronous request processing.

Exactly as for DeferredResult the application can produce the result from a thread of its choice.
``` java
@RestController
@RequestMapping("nonblocking")
public class NonBlockingController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NonBlockingController.class);

    @Autowired
    private ApiClient apiClient;

    @RequestMapping("supplyasync/{input}")
    public CompletableFuture<String> supplyAsync(@PathVariable final String input) {
        LOGGER.debug("Using CompletableFuture.supplyAsync(...)");
        return CompletableFuture.supplyAsync(() -> apiClient.apiCall(input));
    }
}
``` 

Logs from requesting the endpoint:
```
2017-08-23 15:19:01.854 [XNIO-2 I/O-2] DEBUG io.undertow.request - Matched prefix path /tutorial-async for path /tutorial-async/nonblocking/supplyasync/test
2017-08-23 15:19:01.857 [XNIO-2 task-8] DEBUG s.c.a.e.n.NonBlockingController - Using CompletableFuture.supplyAsync(...)
2017-08-23 15:19:01.858 [ForkJoinPool.commonPool-worker-3] DEBUG s.c.api.example.service.ApiClient - Blocking API call
2017-08-23 15:19:01.858 [ForkJoinPool.commonPool-worker-3] DEBUG s.c.a.e.resource.ExternalResource - Do long running task...
2017-08-23 15:19:02.862 [ForkJoinPool.commonPool-worker-3] DEBUG s.c.a.e.resource.ExternalResource - ...long running task done!
```

#### <a name="request-lifecycle-nonblocking-controller-blocking-service"></a>2.1.4 Request lifecycle
Now we have been looking at various ways of reaching non-blocking controllers using Spring Framework. Lets take a look at the request lifecycle when using a non-blocking controller.

![Request lifecycle - Non-blocking controller with blocking api client][request-lifecycle-nonblocking-controller-blocking-service]

![Unhappy man][unhappy-man]

It looks like our angry fellow here is correct, doesn't it?!

So, what we have actually done so far is just moved the problem a couple of steps with more complexity. Leaving throughput unchanged. 

## <a name="resource-client"></a>3. Resource client
What we have to do is calling our slow resources in a non-blocking manner. Then we will have gained higher throughput and a more scalable application.

### <a name="blocking-vs-non-blocking-resource"></a>3.1 Blocking vs. Non-blocking resource client
A blocking I/O client is very much the same as for a server. The model is one thread per connection. For NIO instead we want a few number of dispatch threads 
to dispatch I/O events (connected, inputready, outputready, timeout, disconnected). The idea is to not hold on to any expensive I/O resource (e.g. a http connection)
and instead free the connection for others to use when waiting for a response event.

### <a name="non-blocking-http-client"></a>3.2 Non-blocking http-client
In our case the most interesting resource client would be a __non-blocking http-client__ since we are mostly consuming various REST API:s.
Some of which are really fast and some are annoyingly slow. It would be a good fit consuming those services in a non-blocking I/O manner.

#### <a name="async-rest-template"></a>3.2.1 [AsyncRestTemplate](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/client/AsyncRestTemplate.html)
Spring's central class for asynchronous client-side HTTP access.
By default AsyncRestTemplate relies on standard JDK facilities to establish HTTP connections. You can switch to use a different HTTP library:
 - [HttpUrlConnection](https://docs.oracle.com/javase/8/docs/api/java/net/HttpURLConnection.html)
 - [Apache HttpComponents](https://hc.apache.org/index.html)
 - [Netty](http://netty.io/)
 - [OkHttp](http://square.github.io/okhttp/)

This is done using a [AsyncClientHttpRequestFactory](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/client/AsyncClientHttpRequestFactory.html)

__Gradle config__
``` groovy
dependencies {
    ...
    
    // Apache HTTP Client
    compile group: 'org.apache.httpcomponents', name: 'httpclient', version: '4.5.3'
    compile group: 'org.apache.httpcomponents', name: 'httpasyncclient', version: '4.1.3'
    
    ...
}

```

__Configuring AsyncRestTemplate for using Apache HttpAsyncClient__
``` java
@Configuration
public class HttpConfig {

    private static final int DEFAULT_MAX_TOTAL_CONNECTIONS = 100;
    private static final int DEFAULT_READ_TIMEOUT_MILLISECONDS = 60 * 1000;

    @Bean
    public AsyncRestTemplate asyncRestTemplate() {
        return new AsyncRestTemplate(
                asyncHttpRequestFactory());
    }

    @Bean
    public AsyncClientHttpRequestFactory asyncHttpRequestFactory() {
        return new HttpComponentsAsyncClientHttpRequestFactory(
                asyncHttpClient());
    }

    @Bean
    public CloseableHttpAsyncClient asyncHttpClient() {
        try {
            final PoolingNHttpClientConnectionManager connectionManager = new PoolingNHttpClientConnectionManager(
                    new DefaultConnectingIOReactor(IOReactorConfig.DEFAULT));
            connectionManager.setMaxTotal(DEFAULT_MAX_TOTAL_CONNECTIONS);
            final RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(DEFAULT_READ_TIMEOUT_MILLISECONDS)
                    .build();

            return HttpAsyncClientBuilder
                    .create().setConnectionManager(connectionManager)
                    .setDefaultRequestConfig(config).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
```

The AsyncRestTemplate returns [ListenableFuture](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/util/concurrent/ListenableFuture.html) as a result of http requests. Why not Java 8 CompletableFuture? The reason for this is backwards compatibility. Due to the fact of lots of users still using Java 7 they have decided not to support this feature just yet.

__Convert Spring's ListenableFuture to a CompletableFuture__

``` java
public class FuturesUtils {

    public static <T> CompletableFuture<T> toCompletableFuture(
            final ListenableFuture<T> listenableFuture
    ) {
        final CompletableFuture<T> completable = new CompletableFuture<T>() {
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                // propagate cancel to the listenable future
                boolean result = listenableFuture.cancel(mayInterruptIfRunning);
                super.cancel(mayInterruptIfRunning);
                return result;
            }
        };

        listenableFuture.addCallback(new ListenableFutureCallback<T>() {
            @Override
            public void onSuccess(T result) {
                completable.complete(result);
            }

            @Override
            public void onFailure(Throwable t) {
                completable.completeExceptionally(t);
            }
        });
        return completable;
    }
}
```
#### <a name="unirest-for-java"></a>3.2.2 Unirest for Java
[Unirest for Java](http://unirest.io/java.html)
__Gradle config__
``` groovy
dependencies {
    ...
    
    // Apache HTTP Async Client
    compile group: 'org.apache.httpcomponents', name: 'httpclient', version: '4.5.3'
    compile group: 'org.apache.httpcomponents', name: 'httpasyncclient', version: '4.1.3'
    compile group: 'org.apache.httpcomponents', name: 'httpmime', version: '4.5.3'
    
    // Simplified, lightweight HTTP client library that uses Apache HTTP client
    compile group: 'com.mashape.unirest', name: 'unirest-java', version: '1.4.9'
    
    ...
}

```

__Configure Unirest to be used by your Spring application__
``` java
@Configuration
public class UnirestHttpClientConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnirestHttpClientConfig.class);

    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @PostConstruct
    void init() {
        LOGGER.info("Start configuring Unirest...");
        Unirest.setConcurrency(200, 20);
        Unirest.setTimeouts(SECONDS.toMillis(10), SECONDS.toMillis(60));
        // Register Spring's objectmapper to be used by Unirest
        Unirest.setObjectMapper(new com.mashape.unirest.http.ObjectMapper() {
            @Override
            public <T> T readValue(final String value, final Class<T> valueType) {
                try {
                    return jacksonObjectMapper.readValue(value, valueType);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public String writeValue(final Object value) {
                try {
                    return jacksonObjectMapper.writeValueAsString(value);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @PreDestroy
    void shutdown() throws IOException {
        Unirest.shutdown();
    }
}
```

__Use the callback to create a CompletableFuture__
``` java
public class UnirestHttpClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnirestHttpClient.class);

    public CompletableFuture<HttpResponse<String>> getAsync(final String url, final Map<String, String> headers) {
        final CompletableFuture<HttpResponse<String>> completable = new CompletableFuture<>();
        Unirest.get(url)
                .headers(headers)
                .asStringAsync(new Callback<String>() {
                    @Override
                    public void completed(final HttpResponse<String> response) {
                        LOGGER.debug("response {}", response.getStatus());
                        LOGGER.trace("response {}, {}", response.getStatus(), response.getBody());
                        completable.complete(response);
                    }

                    @Override
                    public void failed(final UnirestException ex) {
                        completable.completeExceptionally(ex);
                    }

                    @Override
                    public void cancelled() {
                        completable.cancel(true);
                    }
                });
        return completable;
    }
}
```

## <a name="combine-non-blockling-restcontroller-with-http-client"></a>4. Combine non-blocking RestController with http-client
``` java
@RestController
@RequestMapping("nonblocking")
public class NonBlockingController {

    @Autowired
    private UnirestHttpClient unirestHttpClient;

    @GetMapping
    public CompletableFuture<ResponseEntity<String>> get(
            final HttpServletRequest request,
            @RequestParam final MultiValueMap<String, String> params) {
            ...
        return unirestHttpClient.getAsync(fromHttpUrl(baseUrl).path(path).queryParams(params).toUriString(), headers)
                .thenApply(Functions::transformResponse);
    }
    ...
}
```

Logs from running non-blocking controller together with non-blocking http-client:
```
2017-08-24 15:36:18.618 [XNIO-2 I/O-3] DEBUG io.undertow.request - Matched prefix path /tutorial-async for path /tutorial-async/
2017-08-24 15:36:18.804 [XNIO-2 task-1] DEBUG s.c.api.controller.ProxyController - Request path /
2017-08-24 15:36:18.851 [XNIO-2 task-1] DEBUG se.cygni.api.client.HttpClient - Request headers: {}
2017-08-24 15:36:19.856 [I/O dispatcher 1] DEBUG se.cygni.api.client.HttpClient - Response 200 with headers {...}
2017-08-24 15:36:19.856 [I/O dispatcher 1] DEBUG se.cygni.api.controller.Functions - Responding with status 200
```

### <a name="request-lifecycle-nonblocking-controller-nonblocking-service"></a>4.1 Request lifecycle
Now we have a request lifecycle looking like the following

![Request lifecycle - Non-blocking controller and non-blocking api client][request-lifecycle-nonblocking-controller-nonblocking-service]

## <a name="summary-further-readings"></a>5. Summary and further readings
![Happy man][happy-man]

So are there any scenarios when CompletableFuture's are not a good fit? Well, when there are interactions with many underlying services and results should be combined and transformed CompletableFutures tend to generate very complex and hard to understand structures.
And also, if there's no actual need in terms of performance requirements. Why make things more complicated than you have to. You will spin up a couple of additional threads and you will make things more complicated to debug etc. There is no silver bullet.

Then it could be a good idea to look at alternatives like [RxJava](https://github.com/ReactiveX/RxJava) or [Akka Streams](http://doc.akka.io/docs/akka/current/java/stream/index.html)

You should also learn about [backpressure](https://medium.com/@jayphelps/backpressure-explained-the-flow-of-data-through-software-2350b3e77ce7) e.g. how to handle when one server is sending requests to another faster than it can process them.


[request-lifecycle-blocking-controller]: https://raw.githubusercontent.com/partjarnberg/tutorials/images/async-http-spring-boot/request-lifecycle-blocking-controller.png "Request lifecycle with blocking controller"
[request-lifecycle-nonblocking-controller-blocking-service]: https://raw.githubusercontent.com/partjarnberg/tutorials/images/async-http-spring-boot/request-lifecycle-nonblocking-controller-blocking-service.png "Request lifecycle with non-blocking controller but with blocking service"
[request-lifecycle-nonblocking-controller-nonblocking-service]: https://raw.githubusercontent.com/partjarnberg/tutorials/images/async-http-spring-boot/request-lifecycle-nonblocking-controller-nonblocking-service.png "Request lifecycle with non-blocking controller and with non-blocking service"
[unhappy-man]: https://raw.githubusercontent.com/partjarnberg/tutorials/images/async-http-spring-boot/complaining-man.png "Unhappy man"
[happy-man]: https://raw.githubusercontent.com/partjarnberg/tutorials/images/async-http-spring-boot/happy-man.png "Happy man"
