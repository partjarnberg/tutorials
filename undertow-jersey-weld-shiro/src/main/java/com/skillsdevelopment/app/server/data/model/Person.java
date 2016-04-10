package com.skillsdevelopment.app.server.data.model;

import java.util.UUID;

public class Person {
    private final UUID uuid;
    private final String firstName;
    private final String lastName;
    private final String email;

    private Person(final UUID uuid, final String firstName, final String lastName, final String email) {
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Person person = (Person) o;

        return uuid.equals(person.uuid);

    }

    @Override
    public String toString() {
        return "Person{" +
                "uuid=" + uuid +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    public static class Builder {
        private UUID uuid;
        private String firstName;
        private String lastName;
        private String email;

        private Builder() {}

        public Builder uuid(final UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder withRandomUUID() {
            this.uuid = UUID.randomUUID();
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

        public Builder email(final String email) {
            this.email = email;
            return this;
        }

        public Person build() {
            return new Person(uuid, firstName, lastName, email);
        }
    }
}
