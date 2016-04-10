package com.skillsdevelopment.app.server.data;

import com.skillsdevelopment.app.server.data.model.Job;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public interface JobStore {
    Set<Job> getAll(final UUID personId);
    void add(final Job job);
    void addAll(final Collection<Job> jobs);
}
