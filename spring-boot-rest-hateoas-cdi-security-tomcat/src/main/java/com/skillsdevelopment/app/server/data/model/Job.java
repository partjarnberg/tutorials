package com.skillsdevelopment.app.server.data.model;

import java.util.UUID;

public class Job {
    private final UUID personId;
    private final UUID employerId;
    private final String employerName;
    private final String profession;

    public Job(final UUID personId, final UUID employerId, final String employerName, final String profession) {
        this.personId = personId;
        this.employerId = employerId;
        this.employerName = employerName;
        this.profession = profession;
    }

    public UUID getPersonId() {
        return personId;
    }

    public UUID getEmployerId() {
        return employerId;
    }

    public String getEmployerName() {
        return employerName;
    }

    public String getProfession() {
        return profession;
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

        Job job = (Job) o;

        if (!personId.equals(job.personId)) {
            return false;
        }
        if (!employerId.equals(job.employerId)) {
            return false;
        }
        return profession.equals(job.profession);

    }

    @Override
    public int hashCode() {
        int result = personId.hashCode();
        result = 31 * result + employerId.hashCode();
        result = 31 * result + profession.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Job{" +
                "personId=" + personId +
                ", employerId=" + employerId +
                ", employerName='" + employerName + '\'' +
                ", profession='" + profession + '\'' +
                '}';
    }

    public static class Builder {
        private UUID personId;
        private UUID employerId;
        private String employerName;
        private String profession;

        private Builder() {}

        public Builder personId(final UUID personId) {
            this.personId = personId;
            return this;
        }

        public Builder employerId(final UUID employerId) {
            this.employerId = employerId;
            return this;
        }

        public Builder employerName(final String employerName) {
            this.employerName = employerName;
            return this;
        }

        public Builder profession(final String profession) {
            this.profession = profession;
            return this;
        }

        public Job build() {
            return new Job(personId, employerId, employerName, profession);
        }
    }
}
