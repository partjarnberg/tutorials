package com.skillsdevelopment.app.server.data;

import com.skillsdevelopment.app.server.data.model.Person;

import java.util.Set;
import java.util.UUID;

public interface PersonStore {
    Set<Person> getAll(final int offset, final int limit);
    Person get(final UUID id);
    void add(final Person person);
}