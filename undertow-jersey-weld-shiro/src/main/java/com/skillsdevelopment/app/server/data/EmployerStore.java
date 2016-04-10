package com.skillsdevelopment.app.server.data;

import com.skillsdevelopment.app.server.data.model.Employer;

import java.util.Set;
import java.util.UUID;

public interface EmployerStore {
    Set<Employer> getAll(final int offset, final int limit);
    Employer get(final UUID id);
    void add(final Employer employer);
}