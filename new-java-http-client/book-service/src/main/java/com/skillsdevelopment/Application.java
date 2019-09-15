package com.skillsdevelopment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.skillsdevelopment.rest.ExceptionHandler;
import com.skillsdevelopment.rest.RatelimiterHandler;
import com.skillsdevelopment.rest.RequestUtil;
import com.skillsdevelopment.rest.representation.BookRepresentation;
import com.skillsdevelopment.rest.representation.BooksRepresentation;
import com.skillsdevelopment.server.BookService;
import com.skillsdevelopment.server.InmemoryBookRepository;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.UUID;

import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
import static io.undertow.UndertowOptions.ENABLE_HTTP2;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.SECONDS;

public class Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
    private static final int DEFAULT_PORT = 8080;
    private static final int DELAY_UPPER_BOUND = 5;
    private static final String DEFAULT_HOST = "0.0.0.0";

    public static void main(String[] args) throws ParseException {
        final Random random = new Random();
        final RequestUtil requestUtil = new RequestUtil(createObjectMapper());
        final BookService service = new BookService(new InmemoryBookRepository());

        final Undertow server = Undertow.builder()
                .setServerOption(ENABLE_HTTP2, true)
                .addHttpListener(extractHttpPort(args), DEFAULT_HOST).setHandler(
                        Handlers.path()
                        .addPrefixPath("/api", new ExceptionHandler(requestUtil,
                                new RatelimiterHandler(requestUtil, new RoutingHandler()
                                .get("/books/{id}", exchange -> {
                                    sleepUninterruptibly(random.nextInt(DELAY_UPPER_BOUND), SECONDS);
                                    final UUID id = parseId(exchange);
                                    requestUtil.sendOk(exchange, new BookRepresentation(id, service.get(id)));
                                })
                                .get("/books", exchange -> {
                                    sleepUninterruptibly(random.nextInt(DELAY_UPPER_BOUND), SECONDS);
                                    final String query = parseQuery(exchange);
                                    requestUtil.sendOk(exchange, new BooksRepresentation(service.query(query)));
                                })
                                .setFallbackHandler(requestUtil::sendNotFound)
                        )))
                        .addPrefixPath("/static", Handlers.resource(
                                new ClassPathResourceManager(Application.class.getClassLoader())))
                ).build();
        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("Stopping HTTP server...");
            server.stop();
        }));
    }

    private static UUID parseId(final HttpServerExchange exchange) {
        return UUID.fromString(ofNullable(exchange.getQueryParameters()
                .get("id").getFirst()).orElseThrow(IllegalStateException::new));
    }

    private static String parseQuery(final HttpServerExchange exchange) {
        return exchange.getQueryParameters()
                .get("query").getFirst();
    }

    private static ObjectMapper createObjectMapper() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
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
