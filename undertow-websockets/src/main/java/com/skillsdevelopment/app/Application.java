package com.skillsdevelopment.app;

import com.skillsdevelopment.app.web.WebSocketEndpoint;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.websockets.jsr.WebSocketDeploymentInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;

public class Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(final String[] args) throws ServletException {
        final DeploymentInfo deployment = Servlets.deployment()
                .setContextPath("/my-app")
                .setDeploymentName("my-app.war")
                .addServletContextAttribute(WebSocketDeploymentInfo.ATTRIBUTE_NAME, new WebSocketDeploymentInfo()
                        .addEndpoint(WebSocketEndpoint.class))
                .setClassLoader(Application.class.getClassLoader());

        final DeploymentManager manager = Servlets.defaultContainer().addDeployment(deployment);
        manager.deploy();

        final PathHandler pathHandler = Handlers.path(Handlers.redirect("/my-app"))
                .addPrefixPath("/my-app", manager.start());

        LOGGER.info("Starting server, listening on port {}", 8080);
        final Undertow server = Undertow.builder()
                .addHttpListener(8080, "0.0.0.0")
                .setHandler(pathHandler)
                .build();
        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("Stopping CDI and HTTP server...");
            server.stop();
        }));
    }
}
