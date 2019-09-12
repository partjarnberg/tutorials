package com.skillsdevelopment.representation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorRepresentation {
    private final String error;

    @JsonCreator
    public ErrorRepresentation(@JsonProperty("error") final String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
