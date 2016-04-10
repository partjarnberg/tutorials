package com.skillsdevelopment.app.web;

import javax.websocket.CloseReason;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/")
public class WebSocketEndpoint {

    @OnOpen
    public void connect(final Session session) {

    }

    @OnMessage
    public void message(final String message, final Session session) {

    }

    public void close(final CloseReason closeReason, final Session session) {

    }
}
