package com.skillsdevelopment.server.model;

import java.util.Objects;
import java.util.UUID;

public class Student {
    private final UUID id;
    private final String firstName;
    private final String lastName;

    private Student(final UUID id, final String firstName, final String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public UUID getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Student student = (Student) o;
        return Objects.equals(id, student.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static Builder builder() {
        return new Builder();
    }
        
    public static class Builder {
        private UUID id;
        private String firstName;
        private String lastName;

        public Builder id(final UUID id) {
            this.id = id;
            return this;
        }

        public Builder firstName(final String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(final String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Student build() {
            return new Student(id, firstName, lastName);
        }
    }
}
