package com.skillsdevelopment.app.web.representation;

import com.skillsdevelopment.app.server.data.model.Employer;
import com.skillsdevelopment.app.web.resource.EmployerResource;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class EmployerRepresentation extends ResourceSupport {
    private final String name;
    private final String email;

    public EmployerRepresentation(final Employer employer) {
        this.add(linkTo(methodOn(EmployerResource.class).getEmployer(employer.getUuid().toString())).withSelfRel());
        this.name = employer.getName();
        this.email = employer.getEmail();
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
