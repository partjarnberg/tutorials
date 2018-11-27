package com.skillsdevelopment;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.undertow.Undertow;
import io.undertow.server.RoutingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

import static io.undertow.UndertowOptions.ENABLE_HTTP2;
import static io.undertow.util.Headers.CONTENT_TYPE;
import static io.undertow.util.StatusCodes.OK;

public class Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        final Car jaguar = new Car("Jaguar", "E-type",
                2, 2, 1234);
        final ObjectMapper objectMapper = new ObjectMapper();

        final Undertow server = Undertow.builder()
                .setServerOption(ENABLE_HTTP2, true)
                .addHttpListener(8080, "0.0.0.0", new RoutingHandler()
                        .get("/", dispatcher -> dispatcher.dispatch(exchange -> {
                            exchange.setStatusCode(OK);
                            exchange.getResponseHeaders().put(CONTENT_TYPE, "application/json;charset=utf-8");
                            exchange.getResponseSender().send(ByteBuffer.wrap(objectMapper.writeValueAsBytes(jaguar)));
                        })))
                .build();
        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("Stopping HTTP server...");
            server.stop();
        }));
    }
}
