package com.skillsdevelopment.app.server.data;

import com.google.common.collect.ImmutableSet;
import com.skillsdevelopment.app.server.data.model.Person;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class InMemoryPersonStore implements PersonStore {
    private SortedMap<UUID, Person> persons = new TreeMap<>(UUID::compareTo);

    @Override
    public SortedSet<Person> getAll(final int offset, final int limit) {
        return persons.entrySet().stream()
                .skip(offset)
                .limit(limit)
                .map(Map.Entry::getValue)
                .collect(Collectors.toCollection(() ->
                        new TreeSet<>((first, second) ->
                                first.getUuid().compareTo(second.getUuid()))));
    }

    @Override
    public Person get(final UUID id) {
        return persons.get(id);
    }

    @Override
    public void add(final Person person) {
        persons.put(person.getUuid(), person);
    }

    @PostConstruct
    private void init() {
        ImmutableSet.of(
                Person.builder()
                        .firstName("John").lastName("Doe")
                        .email("john.doe@mail.com")
                        .withRandomUUID().build(),
                Person.builder()
                        .firstName("Kirk").lastName("Mason")
                        .email("kirk.mason@mail.com")
                        .withRandomUUID().build(),
                Person.builder()
                        .firstName("Earlene").lastName("Walker")
                        .email("earlene.walker@mail.com")
                        .withRandomUUID().build(),
                Person.builder()
                        .firstName("Kenneth").lastName("Finnegan")
                        .email("kenneth.finnegan@mail.com")
                        .withRandomUUID().build(),
                Person.builder()
                        .firstName("William").lastName("Dickerson")
                        .email("william.dickerson@mail.com")
                        .withRandomUUID().build()).forEach(this::add);
    }
}
