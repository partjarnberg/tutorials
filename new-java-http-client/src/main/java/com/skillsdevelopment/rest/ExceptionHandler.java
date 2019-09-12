package com.skillsdevelopment.rest;


import com.skillsdevelopment.rest.representation.ErrorRepresentation;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionHandler implements HttpHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandler.class);

    private final RequestUtil requestUtil;
    private final HttpHandler next;

    public ExceptionHandler(final RequestUtil requestUtil, final HttpHandler next) {
        this.requestUtil = requestUtil;
        this.next = next;
    }

    @Override
    public void handleRequest(final HttpServerExchange exchange) {
        try {
            LOGGER.debug("Handling request...");
            next.handleRequest(exchange);
        } catch (IllegalStateException | IllegalArgumentException e) {
            requestUtil.sendBadRequest(exchange, new ErrorRepresentation(e.getMessage()));
        } catch (Exception e) {
            LOGGER.error("Unexpected error occurred.", e.getMessage());
            requestUtil.sendInternalServerError(exchange);
        }
    }
}
