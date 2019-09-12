package com.skillsdevelopment.representation;

import com.skillsdevelopment.server.model.Course;

import java.util.UUID;

public class CourseRepresentation {
    private final UUID id;
    private final String name;
    private final String description;

    public CourseRepresentation(final Course course) {
        id = course.getId();
        name = course.getName();
        description = course.getDescription();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
