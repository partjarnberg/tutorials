package com.skillsdevelopment.server;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class JsonUtil {
    private static final JsonUtil DEFAULT_SERIALIZER;
    private final ObjectMapper mapper;
    private final ObjectWriter prettyWriter;

    static {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .enable(SerializationFeature.INDENT_OUTPUT)
                .enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        DEFAULT_SERIALIZER = new JsonUtil(mapper);
    }

    private JsonUtil(final ObjectMapper mapper) {
        this.mapper = mapper;
        this.prettyWriter = mapper.writerWithDefaultPrettyPrinter();
    }

    public static JsonUtil serializer() {
        return DEFAULT_SERIALIZER;
    }

    public String toJsonString(final Object o) {
        try {
            return prettyWriter.writeValueAsString(o);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    static Stream<JsonNode> stream(final JsonNode jsonNode) {
        return StreamSupport.stream(jsonNode.spliterator(), false);
    }

    JsonNode nodeFromJson(final String json) {
        try {
            return mapper.readTree(json);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    private static class JsonException extends RuntimeException {
        private JsonException(Exception ex) {
            super(ex);
        }
    }
}
