package com.skillsdevelopment.representation;

import com.skillsdevelopment.server.model.Course;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ParticipantsRepresentation {
    private final UUID id;
    private final List<StudentRepresentation> participants;

    public ParticipantsRepresentation(final Course course) {
        id = course.getId();
        participants = new ArrayList<>(course.getParticipants()).stream()
                .map(StudentRepresentation::new).collect(Collectors.toList());
    }

    public UUID getId() {
        return id;
    }

    public List<StudentRepresentation> getParticipants() {
        return participants;
    }
}
