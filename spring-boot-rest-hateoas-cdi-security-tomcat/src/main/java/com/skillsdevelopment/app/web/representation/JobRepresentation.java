package com.skillsdevelopment.app.web.representation;

import com.skillsdevelopment.app.web.resource.EmployerResource;
import org.springframework.hateoas.ResourceSupport;

import java.util.UUID;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class JobRepresentation extends ResourceSupport {
    private final String profession;
    private final String employer;

    public JobRepresentation(final String profession, final UUID employerId, final String employerName) {
        this.add(linkTo(methodOn(EmployerResource.class).getEmployer(employerId.toString())).withRel("employer"));
        this.profession = profession;
        this.employer = employerName;
    }

    public String getProfession() {
        return profession;
    }

    public String getEmployer() {
        return employer;
    }
}
