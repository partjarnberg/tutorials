package com.skillsdevelopment.app.server.data;

import com.google.common.collect.ImmutableSet;
import com.skillsdevelopment.app.server.data.model.Employer;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InMemoryEmployerStore implements EmployerStore {
    private SortedMap<UUID, Employer> employers = new TreeMap<>(UUID::compareTo);

    @Override
    public Set<Employer> getAll(final int offset, final int limit) {
        return employers.entrySet().stream()
                .skip(offset)
                .limit(limit)
                .map(Map.Entry::getValue)
                .collect(Collectors.toCollection(() ->
                        new TreeSet<>((first, second) ->
                                first.getUuid().compareTo(second.getUuid()))));
    }

    @Override
    public Employer get(final UUID id) {
        return employers.get(id);
    }

    @Override
    public void add(final Employer employer) {
        employers.put(employer.getUuid(), employer);
    }

    @PostConstruct
    private void init() {
        ImmutableSet.of(
                Employer.builder()
                        .withRandomUUID()
                        .name("Fancy Company")
                        .email("contact@fancycompany.com")
                        .build(),
                Employer.builder()
                        .withRandomUUID()
                        .name("Coolest company ever")
                        .email("contact@coolest.com")
                        .build(),
                Employer.builder()
                        .withRandomUUID()
                        .name("Fancy Brand")
                        .email("contact@fancybrand.com")
                        .build(),
                Employer.builder()
                        .withRandomUUID()
                        .name("Software Company")
                        .email("contact@software.com")
                        .build(),
                Employer.builder()
                        .withRandomUUID()
                        .name("Shelf Company")
                        .email("contact@shelf.com")
                        .build(),
                Employer.builder()
                        .withRandomUUID()
                        .name("The tech startup")
                        .email("contact@startup.com")
                        .build(),
                Employer.builder()
                        .withRandomUUID()
                        .name("Ooops")
                        .email("contact@ooops.com")
                        .build(),
                Employer.builder()
                        .withRandomUUID()
                        .name("Cars")
                        .email("contact@cars.com")
                        .build(),
                Employer.builder()
                        .withRandomUUID()
                        .name("Java professionals")
                        .email("contact@javaprofessionals.com")
                        .build(),
                Employer.builder()
                        .withRandomUUID()
                        .name("Hipsters")
                        .email("contact@hipsters.com")
                        .build()).forEach(this::add);
    }
}
