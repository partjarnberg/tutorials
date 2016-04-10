package com.skillsdevelopment.app.web.representation;

import com.skillsdevelopment.app.server.data.model.Job;
import com.skillsdevelopment.app.web.resource.EmployerResource;
import com.skillsdevelopment.app.web.resource.PersonResource;
import org.springframework.hateoas.ResourceSupport;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class JobsRepresentation extends ResourceSupport {
    private final Set<JobRepresentation> jobs = new HashSet<>();

    public JobsRepresentation(final String personId, final Set<Job> jobs) {
        this.add(linkTo(methodOn(PersonResource.class).getJobs(personId)).withSelfRel());
        jobs.forEach(job -> {
            this.jobs.add(new JobRepresentation(job.getProfession(), job.getEmployerId(), job.getEmployerName()));
        });
    }

    public Set<JobRepresentation> getJobs() {
        return jobs;
    }
}
