package com.skillsdevelopment.app.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

@ServerEndpoint("/ws")
public class WebSocketEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketEndpoint.class);

    private Set<Session> clients = new LinkedHashSet<>();

    @OnOpen
    public void connect(final Session session) {
        LOGGER.info("Connected: {}", session);
        clients.add(session);
    }

    @OnMessage
    public void message(final String message, final Session session) {
        LOGGER.info("Message: {}, from: {}", message, session);
        broadcast(session, message);
    }

    @OnClose
    public void close(final CloseReason closeReason, final Session session) {
        LOGGER.info("Connection closed: {}, reason: {} {}", session, closeReason.getCloseCode(), closeReason.getReasonPhrase());
    }

    private void broadcast(final Session sender, final String message) {
        clients.parallelStream()
            .filter(Session::isOpen)
            .filter(s -> !s.equals(sender))
            .forEach(s -> {
                try {
                    LOGGER.info("Sending '{}' to {}", message, s);
                    s.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
    }
}
