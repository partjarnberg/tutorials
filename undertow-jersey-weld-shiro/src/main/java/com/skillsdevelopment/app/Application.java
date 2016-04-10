package com.skillsdevelopment.app;

import com.skillsdevelopment.app.web.JerseyConfig;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import org.apache.commons.cli.*;
import org.apache.shiro.web.env.EnvironmentLoaderListener;
import org.apache.shiro.web.servlet.ShiroFilter;
import org.glassfish.jersey.servlet.ServletContainer;
import org.jboss.weld.environment.servlet.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.DispatcherType;
import javax.servlet.ServletException;

import static io.undertow.servlet.Servlets.*;
import static org.glassfish.jersey.servlet.ServletProperties.JAXRS_APPLICATION_CLASS;

public class Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
    private static final int DEFAULT_HTTP_PORT = 8080;

    public static void main(final String[] args) throws ServletException, ParseException {
        final Options options = new Options();
        options.addOption(Option.builder("httpPort")
                .longOpt("httpPort")
                .required(false)
                .type(Number.class)
                .numberOfArgs(1)
                .desc("http port this server listens on")
                .build());

        final CommandLineParser parser = new DefaultParser();
        final CommandLine cmdLine = parser.parse(options, args);
        final int httpPort = cmdLine.hasOption("httpPort") ?
                ((Number)cmdLine.getParsedOptionValue("httpPort")).intValue() : DEFAULT_HTTP_PORT;

        final DeploymentInfo servletBuilder = deployment()
                .setClassLoader(Application.class.getClassLoader())
                .setResourceManager(new ClassPathResourceManager(Application.class.getClassLoader()))
                .setContextPath("/my-app")
                .setDeploymentName("my-app.war")
                .addServlets(
                        servlet("jerseyServlet", ServletContainer.class)
                                .setLoadOnStartup(1)
                                .addInitParam(JAXRS_APPLICATION_CLASS, JerseyConfig.class.getName())
                                .addMapping("/api/*"))
                .addListener(listener(Listener.class)) // Weld listener
                .addListener(listener(EnvironmentLoaderListener.class)) // Shiro listener
                .addFilter(filter("shiroFilter", ShiroFilter.class))
                .addFilterUrlMapping("shiroFilter", "/*", DispatcherType.REQUEST)
                .addFilterUrlMapping("shiroFilter", "/*", DispatcherType.FORWARD)
                .addFilterUrlMapping("shiroFilter", "/*", DispatcherType.INCLUDE)
                .addFilterUrlMapping("shiroFilter", "/*", DispatcherType.ERROR);


        final DeploymentManager deploymentManager = Servlets.defaultContainer().addDeployment(servletBuilder);
        deploymentManager.deploy();

        final PathHandler pathHandler = Handlers.path(Handlers.redirect("/my-app"))
                .addPrefixPath("/my-app", deploymentManager.start());

        LOGGER.info("Starting server, listening on port {}", httpPort);

        final Undertow server = Undertow
                .builder()
                .addHttpListener(httpPort, "0.0.0.0")
                .setHandler(pathHandler)
                .build();
        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                LOGGER.info("Stopping CDI and HTTP server...");
                server.stop();
            }
        });
    }
}
