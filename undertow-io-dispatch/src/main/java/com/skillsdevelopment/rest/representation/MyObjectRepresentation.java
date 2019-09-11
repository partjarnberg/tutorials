package com.skillsdevelopment.rest.representation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillsdevelopment.server.model.MyObject;

import java.util.UUID;

public class MyObjectRepresentation {
    private final UUID id;
    private final String name;

    @JsonCreator
    public MyObjectRepresentation(@JsonProperty("name") final String name) {
        this.name = name;
        this.id = null;
    }
    
    public MyObjectRepresentation(final MyObject myObject) {
        this.name = myObject.getFanzyName();
        this.id = myObject.getId();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
