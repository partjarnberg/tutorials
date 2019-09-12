package com.skillsdevelopment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.skillsdevelopment.rest.ExceptionHandler;
import com.skillsdevelopment.rest.MyController;
import com.skillsdevelopment.rest.RequestDispatcher;
import com.skillsdevelopment.rest.RequestUtil;
import com.skillsdevelopment.server.AsyncHttpClient;
import io.undertow.server.HttpHandler;
import io.undertow.server.RoutingHandler;

import java.net.URI;
import java.util.function.Function;

class Configuration {
    private static final Configuration INSTANCE = new Configuration();
    private final HttpHandler root;

    private Configuration() {
        final RequestUtil requestUtil = new RequestUtil(createObjectMapper());
        final MyController myController = new MyController(requestUtil, asynchHttpClient());

        root = new RequestDispatcher(new ExceptionHandler(requestUtil, new RoutingHandler()
                .get("/api/{id}", myController::get)
                .get("api/slow/{id}", myController::intentionallySlow)
                .setFallbackHandler(myController::notFound)));
    }

    HttpHandler getRoot() {
        return root;
    }

    static Configuration getInstance() {
        return INSTANCE;
    }

    private ObjectMapper createObjectMapper() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    private AsyncHttpClient asynchHttpClient() {
        return new AsyncHttpClient((Function<String, URI>) id ->
                URI.create("/api/slow/{id}".replace("{id}", id)));
    }
}
