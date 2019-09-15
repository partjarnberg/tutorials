package com.skillsdevelopment.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillsdevelopment.rest.representation.ErrorRepresentation;
import com.skillsdevelopment.rest.representation.StudentRepresentation;
import io.undertow.server.HttpServerExchange;

import java.io.IOException;
import java.util.function.Consumer;

import static io.undertow.util.Headers.CONTENT_TYPE;
import static io.undertow.util.Headers.LOCATION;
import static io.undertow.util.StatusCodes.BAD_REQUEST;
import static io.undertow.util.StatusCodes.CREATED;
import static io.undertow.util.StatusCodes.INTERNAL_SERVER_ERROR;
import static io.undertow.util.StatusCodes.NOT_FOUND;
import static io.undertow.util.StatusCodes.OK;
import static io.undertow.util.StatusCodes.TOO_MANY_REQUESTS;
import static java.nio.charset.StandardCharsets.UTF_8;

public class RequestUtil {
    private final ObjectMapper objectMapper;

    public RequestUtil(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> void parseRequestBody(final HttpServerExchange exchange, final Class<T> clazz, final Consumer<T> consumer) {
        exchange.getRequestReceiver().receiveFullBytes((ignore, data) -> {
            try {
                consumer.accept(objectMapper.readValue(data, clazz));
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        });
    }

    public void sendCreated(final HttpServerExchange exchange, final StudentRepresentation studentRepresentation) {
        exchange.getResponseHeaders().put(LOCATION, exchange.getRequestPath() + "/" + studentRepresentation.getId());
        sendJson(exchange, studentRepresentation, CREATED);
    }

    public void sendOk(final HttpServerExchange exchange, final Object o) {
        sendJson(exchange, o, OK);
    }

    public void sendNotFound(final HttpServerExchange exchange) {
        sendJson(exchange, new ErrorRepresentation("Hoodeladi - Page Not Found!!"), NOT_FOUND);
    }

    void sendTooManyRequests(final HttpServerExchange exchange, final ErrorRepresentation error) {
        sendJson(exchange, error, TOO_MANY_REQUESTS);
    }

    void sendBadRequest(final HttpServerExchange exchange, final ErrorRepresentation error) {
        sendJson(exchange, error, BAD_REQUEST);
    }

    void sendInternalServerError(final HttpServerExchange exchange) {
        exchange.setStatusCode(INTERNAL_SERVER_ERROR);
    }

    private void sendJson(final HttpServerExchange exchange, final Object o, final int statusCode) {
        try {
            exchange.setStatusCode(statusCode);
            exchange.getResponseHeaders().put(CONTENT_TYPE, "application/json;charset=utf-8");
            exchange.getResponseSender().send(objectMapper.writeValueAsString(o), UTF_8);
        } catch (Exception e) {
            sendInternalServerError(exchange);
        }
    }
}
