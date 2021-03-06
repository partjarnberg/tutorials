package com.skillsdevelopment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.skillsdevelopment.rest.ExceptionHandler;
import com.skillsdevelopment.rest.RatelimiterHandler;
import com.skillsdevelopment.rest.RequestUtil;
import com.skillsdevelopment.rest.representation.CourseRepresentation;
import com.skillsdevelopment.rest.representation.CoursesRepresentation;
import com.skillsdevelopment.rest.representation.ParticipantsRepresentation;
import com.skillsdevelopment.rest.representation.StudentRepresentation;
import com.skillsdevelopment.rest.representation.StudentsRepresentation;
import com.skillsdevelopment.server.UniversityService;
import com.skillsdevelopment.server.model.Course;
import com.skillsdevelopment.server.model.Student;
import io.undertow.Undertow;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;
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

import static com.google.common.base.Preconditions.checkNotNull;
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
        final UniversityService service = new UniversityService();

        final Undertow server = Undertow.builder()
                .setServerOption(ENABLE_HTTP2, true)
                .addHttpListener(extractHttpPort(args), DEFAULT_HOST, new ExceptionHandler(requestUtil, new RatelimiterHandler(requestUtil, new RoutingHandler()
                        .get("/api/students", exchange -> {
                            sleepUninterruptibly(random.nextInt(DELAY_UPPER_BOUND), SECONDS);
                            requestUtil.sendOk(exchange, new StudentsRepresentation(service.getAllStudents()));
                        })
                        .post("/api/students", exchange -> {
                            sleepUninterruptibly(random.nextInt(DELAY_UPPER_BOUND), SECONDS);
                            requestUtil.parseRequestBody(exchange, StudentRepresentation.class, studentRepresentation -> {
                                final String[] split = studentRepresentation.getName().split(" ");
                                final Student createdStudent = Student.builder().firstName(split[0]).lastName(split[1]).id(UUID.randomUUID()).build();
                                service.addStudent(createdStudent);
                                requestUtil.sendCreated(exchange, new StudentRepresentation(createdStudent));
                            });
                        })
                        .get("/api/students/{id}", exchange -> {
                            sleepUninterruptibly(random.nextInt(DELAY_UPPER_BOUND), SECONDS);
                            requestUtil.sendOk(exchange, new StudentRepresentation(service.getStudent(parseId(exchange))));
                        })
                        .get("/api/students/{id}/courses", exchange -> {
                            sleepUninterruptibly(random.nextInt(DELAY_UPPER_BOUND), SECONDS);
                            requestUtil.sendOk(exchange, new CoursesRepresentation(service.getCoursesFor(service.getStudent(parseId(exchange)))));
                        })
                        .get("/api/courses", exchange -> {
                            sleepUninterruptibly(random.nextInt(DELAY_UPPER_BOUND), SECONDS);
                            requestUtil.sendOk(exchange, new CoursesRepresentation(service.getAllCourses()));
                        })
                        .get("/api/courses/{id}", exchange -> {
                            sleepUninterruptibly(random.nextInt(DELAY_UPPER_BOUND), SECONDS);
                            requestUtil.sendOk(exchange, new CourseRepresentation(service.getCourse(parseId(exchange))));
                        })
                        .get("/api/courses/{id}/participants", exchange -> {
                            sleepUninterruptibly(random.nextInt(DELAY_UPPER_BOUND), SECONDS);
                            requestUtil.sendOk(exchange, new ParticipantsRepresentation(service.getCourse(parseId(exchange))));
                        })
                        .post("/api/courses/{id}/participants", exchange -> {
                            sleepUninterruptibly(random.nextInt(DELAY_UPPER_BOUND), SECONDS);
                            requestUtil.parseRequestBody(exchange, StudentRepresentation.class, studentRepresentation -> {
                                final UUID courseId = parseId(exchange);
                                final Course course = service.getCourse(courseId);
                                checkNotNull(course, "Course  " + courseId + " cannot be found!");

                                final Student student = service.getStudent(studentRepresentation.getId());
                                checkNotNull(student, "Student " + studentRepresentation.getId() + " cannot be found!");

                                service.participate(student, course);
                                requestUtil.sendOk(exchange, new ParticipantsRepresentation(course));
                            });
                        })
                        .delete("/api/courses/{id}/participants", exchange -> {
                            sleepUninterruptibly(DELAY_UPPER_BOUND, SECONDS);
                            requestUtil.parseRequestBody(exchange, StudentRepresentation.class, studentRepresentation -> {
                                final UUID courseId = parseId(exchange);
                                final Course course = service.getCourse(courseId);
                                checkNotNull(course, "Course  " + courseId + " cannot be found!");

                                final Student student = service.getStudent(studentRepresentation.getId());
                                checkNotNull(student, "Student " + studentRepresentation.getId() + " cannot be found!");

                                service.terminate(student, course);
                                requestUtil.sendOk(exchange, new ParticipantsRepresentation(course));
                            });
                        })
                        .setFallbackHandler(requestUtil::sendNotFound)

                ))).build();
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
