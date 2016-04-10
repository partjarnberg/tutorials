package com.skillsdevelopment.app.server.data;

import com.google.common.collect.*;
import com.skillsdevelopment.app.server.data.model.Employer;
import com.skillsdevelopment.app.server.data.model.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

@Service
public class InMemoryJobStore implements JobStore {
    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryJobStore.class);

    @Autowired
    private PersonStore personStore;
    @Autowired
    private EmployerStore employerStore;

    private Multimap<UUID, Job> jobs = Multimaps.synchronizedMultimap(ArrayListMultimap.create());

    @Override
    public Set<Job> getAll(final UUID personId) {
        return ImmutableSet.copyOf(this.jobs.get(personId));
    }

    @Override
    public void add(final Job job) {
        LOGGER.info("Adding job {}", job);
        jobs.put(job.getPersonId(), job);
    }

    @Override
    public void addAll(final Collection<Job> jobs) {
        jobs.forEach(this::add);
    }

    @PostConstruct
    private void init() {
        final Random random = new Random();
        final String[] professions = {"Software Developer", "Musician",
                "Construction Worker", "Youtube Star", "Engineer", "Stock broker",
                "Bank clerk", "Car salesman", "Clerk", "Doctor", "Oracle"};

        final Set<Employer> employers = employerStore.getAll(0, 50);
        personStore.getAll(0, 50).forEach(p -> {
            employers.stream()
                    .skip(random.nextInt(employers.size()))
                    .limit(5)
                    .forEach(employer -> {
                        add(Job.builder()
                                .personId(p.getUuid())
                                .employerId(employer.getUuid())
                                .employerName(employer.getName())
                                .profession(professions[random.nextInt(professions.length)])
                                .build());
                    });
        });
    }
}
