package com.skillsdevelopment.app.server.data.model;

import java.util.UUID;

public class Employer {
    private final UUID uuid;
    private final String name;
    private final String email;

    public Employer(final UUID uuid, final String name, final String email) {
        this.uuid = uuid;
        this.name = name;
        this.email = email;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
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

        Employer employer = (Employer) o;

        return uuid.equals(employer.uuid);

    }

    @Override
    public String toString() {
        return "Employer{" +
                "uuid=" + uuid +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    public static class Builder {
        private UUID uuid;
        private String name;
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

        public Builder name(final String name) {
            this.name = name;
            return this;
        }

        public Builder email(final String email) {
            this.email = email;
            return this;
        }

        public Employer build() {
            return new Employer(uuid, name, email);
        }
    }
}
