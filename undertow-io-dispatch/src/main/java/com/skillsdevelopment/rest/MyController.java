package com.skillsdevelopment.rest;

import com.skillsdevelopment.rest.representation.MyObjectListRepresentation;
import com.skillsdevelopment.rest.representation.MyObjectRepresentation;
import com.skillsdevelopment.server.MyService;
import com.skillsdevelopment.server.model.MyObject;
import io.undertow.server.HttpServerExchange;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

public class MyController {
    private final MyService myService;
    private final RequestUtil requestUtil;

    public MyController(final MyService myService, final RequestUtil requestUtil) {
        this.requestUtil = requestUtil;
        this.myService = myService;
    }

    public void getObject(final HttpServerExchange exchange) {
        final Optional<MyObject> myObject = myService.findObject(parseId(exchange));
        if (myObject.isEmpty()) {
            requestUtil.sendNotFound(exchange);
        } else {
            requestUtil.sendOk(exchange, new MyObjectRepresentation(myObject.orElseThrow(IllegalStateException::new)));
        }
    }

    public void getAllObjects(final HttpServerExchange exchange) {
        final List<MyObject> myObjects = myService.findAll();
        requestUtil.sendOk(exchange, new MyObjectListRepresentation(
                myObjects.stream().map(MyObjectRepresentation::new)
                        .collect(Collectors.toList())));
    }

    public void createObject(final HttpServerExchange exchange) {
        requestUtil.parseRequestBody(exchange, MyObjectRepresentation.class, myObjectRepresentation -> {
            final MyObject myObject = myService.createObject(myObjectRepresentation.getName());
            requestUtil.sendCreated(exchange, new MyObjectRepresentation(myObject));
        });
    }

    public void notFound(final HttpServerExchange exchange) {
        requestUtil.sendNotFound(exchange);
    }

    private UUID parseId(final HttpServerExchange exchange) {
        return UUID.fromString(ofNullable(exchange.getQueryParameters()
                .get("id").getFirst()).orElseThrow(IllegalStateException::new));
    }
}
