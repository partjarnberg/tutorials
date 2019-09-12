package com.skillsdevelopment.rest;

import com.skillsdevelopment.rest.representation.SomeExpensiveObjectRepresentation;
import com.skillsdevelopment.rest.representation.TwoExpensiveObjectRepresentation;
import com.skillsdevelopment.server.AsyncHttpClient;
import io.undertow.server.HttpServerExchange;

import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.SECONDS;

public class MyController {
    private final RequestUtil requestUtil;
    private final AsyncHttpClient asyncHttpClient;

    public MyController(final RequestUtil requestUtil, final AsyncHttpClient asyncHttpClient) {
        this.requestUtil = requestUtil;
        this.asyncHttpClient = asyncHttpClient;
    }

    public void get(final HttpServerExchange exchange) {
        asyncHttpClient.getAsync(parseId(exchange))
                .thenCombine(asyncHttpClient.getAsync(parseId(exchange)), ((jsonNode, jsonNode2) ->
                        new TwoExpensiveObjectRepresentation(
                                requestUtil.objectFromJson(SomeExpensiveObjectRepresentation.class, jsonNode),
                                requestUtil.objectFromJson(SomeExpensiveObjectRepresentation.class, jsonNode2))))
                .thenAccept(teor -> requestUtil.sendOk(exchange, teor));
    }

    public void intentionallySlow(final HttpServerExchange exchange) {
        sleepUninterruptibly(3, SECONDS);
        requestUtil.sendOk(exchange,
                new SomeExpensiveObjectRepresentation(parseId(exchange),
                        "This is what you have been waiting for..."));
    }

    public void notFound(final HttpServerExchange exchange) {

    }

    private String parseId(final HttpServerExchange exchange) {
        return ofNullable(exchange.getQueryParameters()
                .get("id").getFirst()).orElseThrow(IllegalStateException::new);
    }
}
