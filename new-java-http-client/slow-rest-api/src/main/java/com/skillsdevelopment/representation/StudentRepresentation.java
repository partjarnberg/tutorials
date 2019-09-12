package com.skillsdevelopment.representation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillsdevelopment.server.model.Student;

import java.util.Optional;
import java.util.UUID;

public class StudentRepresentation {
    private final UUID id;
    private final String name;

    public StudentRepresentation(final Student student) {
        id = student.getId();
        name = student.getFirstName() + " " + student.getLastName();
    }

    @JsonCreator
    public StudentRepresentation(@JsonProperty("name") final String name, @JsonProperty("id") final String id) {
        this.name = name;
        this.id = Optional.ofNullable(id).map(UUID::fromString).orElse(null);
    }
    
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
