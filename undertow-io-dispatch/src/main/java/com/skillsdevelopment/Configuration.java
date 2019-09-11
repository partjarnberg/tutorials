package com.skillsdevelopment;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.skillsdevelopment.rest.ExceptionHandler;
import com.skillsdevelopment.rest.MyController;
import com.skillsdevelopment.rest.RequestDispatcher;
import com.skillsdevelopment.rest.RequestUtil;
import com.skillsdevelopment.server.MyService;
import io.undertow.server.HttpHandler;
import io.undertow.server.RoutingHandler;

class Configuration {
    private static final Configuration INSTANCE = new Configuration();
    private final HttpHandler root;

    private Configuration() {
        final RequestUtil requestUtil = new RequestUtil(createObjectMapper());
        final MyController myController = new MyController(new MyService(),
                requestUtil);

        root = new RequestDispatcher(new ExceptionHandler(requestUtil, new RoutingHandler()
                .get("/api/objects", myController::getAllObjects)
                .get("/api/objects/{id}", myController::getObject)
                .post("/api/objects", myController::createObject)
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
        mapper.setSerializationInclusion(Include.NON_NULL)
                .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

}
