package com.skillsdevelopment;


import com.skillsdevelopment.rest.representation.ErrorRepresentation;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionHandler implements HttpHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandler.class);

    private final RequestUtil requestUtil;
    private final HttpHandler next;

    ExceptionHandler(final RequestUtil requestUtil, final HttpHandler next) {
        this.requestUtil = requestUtil;
        this.next = next;
    }

    @Override
    public void handleRequest(final HttpServerExchange exchange) {
        try {
            next.handleRequest(exchange);
        } catch (IllegalStateException | IllegalArgumentException e) {
            LOGGER.debug("Handling faulty request...");
            requestUtil.sendBadRequest(exchange, new ErrorRepresentation(e.getMessage()));
        } catch (Exception e) {
            LOGGER.error("Unexpected error occurred.", e.getMessage());
            requestUtil.sendInternalServerError(exchange);
        }
    }
}
