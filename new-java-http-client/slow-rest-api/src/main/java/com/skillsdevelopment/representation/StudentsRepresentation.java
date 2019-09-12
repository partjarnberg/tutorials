package com.skillsdevelopment.representation;

import com.skillsdevelopment.server.model.Student;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class StudentsRepresentation {
    private final List<StudentRepresentation> students;

    public StudentsRepresentation(final Collection<Student> students) {
        this.students = students.stream()
                .map(StudentRepresentation::new).collect(Collectors.toList());
    }

    public List<StudentRepresentation> getStudents() {
        return students;
    }
}
