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
