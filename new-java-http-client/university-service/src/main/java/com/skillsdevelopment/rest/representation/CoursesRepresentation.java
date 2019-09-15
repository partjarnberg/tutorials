package com.skillsdevelopment.rest.representation;

import com.skillsdevelopment.server.model.Course;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CoursesRepresentation {
    private final List<CourseRepresentation> courses;

    public CoursesRepresentation(final Collection<Course> courses) {
        this.courses = courses.stream()
                .map(CourseRepresentation::new).collect(Collectors.toList());
    }

    public List<CourseRepresentation> getCourses() {
        return courses;
    }
}
