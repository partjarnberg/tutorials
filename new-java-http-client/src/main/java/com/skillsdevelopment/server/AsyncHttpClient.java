package com.skillsdevelopment.server;

import com.fasterxml.jackson.databind.JsonNode;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static java.time.Duration.of;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.net.http.HttpClient.Version.HTTP_2;
import static java.net.http.HttpClient.newBuilder;

public class AsyncHttpClient {
    private final HttpClient httpClient;
    private final JsonUtil serializer;
    private final Function function;

    public AsyncHttpClient(final Function function) {
        this.function = function;
        serializer = JsonUtil.serializer();
        httpClient = newBuilder()
                .version(HTTP_2)
                .followRedirects(Redirect.NORMAL)
                .build();
    }

    @SuppressWarnings("unchecked")
    public CompletableFuture<JsonNode> getAsync(final Object o) {
        final HttpRequest request = HttpRequest.newBuilder((URI) function.apply(o))
                .version(HTTP_2).GET()
                .timeout(of(60, SECONDS))
                .header("Accept", "application/json")
                .header("Access-Control-Allow-Origin", "*")
                .build();

        return httpClient.sendAsync(request, BodyHandlers.ofString()).thenApply(HttpResponse::body)
                .thenApply(serializer::nodeFromJson);
    }
}
