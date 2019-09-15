package com.skillsdevelopment.server.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Course {
    private final UUID id;
    private final String name;
    private final String description;

    private final Set<Student> participants;

    private Course(final UUID id, final String name, final String description, final Set<Student> participants) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.participants = participants;
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

    public Set<Student> getParticipants() {
        return participants;
    }

    public void addParticipant(final Student student) {
        participants.add(student);
    }

    public void removeParticipant(final Student student) {
        participants.remove(student);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private String description;

        public Builder name(final String name) {
            this.name = name;
            return this;
        }

        public Builder description(final String description) {
            this.description = description;
            return this;
        }

        public Course build() {
            return new Course(UUID.randomUUID(), name, description, new HashSet<>());
        }
    }
}
