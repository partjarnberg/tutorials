package com.skillsdevelopment.rest;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class RequestDispatcher implements HttpHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestDispatcher.class);
    private final ExceptionHandler next;

    public RequestDispatcher(final ExceptionHandler next) {
        this.next = next;
    }

    @Override
    public void handleRequest(final HttpServerExchange exchange) {
        if (exchange.isInIoThread()) {
            LOGGER.debug("Exchange in I/O thread. Dispatching...");
            exchange.dispatch(this);
            return;
        }
        LOGGER.debug("Handling request...");
        next.handleRequest(exchange);
    }
}
