package com.skillsdevelopment;

import io.undertow.Undertow;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.undertow.UndertowOptions.ENABLE_HTTP2;

public class Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
    private static final int DEFAULT_PORT = 8080;
    private static final String DEFAULT_HOST = "0.0.0.0";

    public static void main(String[] args) throws ParseException {

        final Undertow server = Undertow.builder()
                .setServerOption(ENABLE_HTTP2, true)
                .addHttpListener(extractHttpPort(args), DEFAULT_HOST, Configuration.getInstance().getRoot())
                .build();
        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("Stopping HTTP server...");
            server.stop();
        }));
    }

    private static int extractHttpPort(String[] args) throws ParseException {
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
        return cmdLine.hasOption("httpPort") ?
                ((Number)cmdLine.getParsedOptionValue("httpPort")).intValue() : DEFAULT_PORT;
    }
}
