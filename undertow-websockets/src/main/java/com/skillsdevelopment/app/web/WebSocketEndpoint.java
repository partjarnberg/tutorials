package com.skillsdevelopment.app.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/ws")
public class WebSocketEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketEndpoint.class);

    @OnOpen
    public void connect(final Session session) {
        LOGGER.info("Connected: {}", session);
    }

    @OnMessage
    public void message(final String message, final Session session) {
        LOGGER.info("Message: {}, from: {}", message, session);
    }

    @OnClose
    public void close(final CloseReason closeReason, final Session session) {
        LOGGER.info("Connection closed: {}, reason: {}", session, closeReason);
    }
}
