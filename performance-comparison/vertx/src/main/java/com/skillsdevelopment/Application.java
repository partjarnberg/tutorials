package com.skillsdevelopment;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    @Override
    public void start(Future<Void> future) {
        final Car jaguar = new Car("Jaguar", "E-type",
                2, 2, 1234);

        final Router router = Router.router(vertx);
        router.get("/").produces("application/json").handler(request ->
                request.response()
                        .putHeader("content-type", "application/json;charset=utf-8")
                        .setStatusCode(200).write(Buffer.buffer(Json.encode(jaguar)))
                        .end());

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(config().getInteger("http.port", 8080), result -> {
                    if (result.succeeded()) {
                        future.complete();
                    } else {
                        future.fail(result.cause());
                    }
                });
    }

    @Override
    public void stop() {
        LOGGER.info("Shutting down application");
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new Application());
    }
}
