package com.skillsdevelopment.app.web.representation;

import com.skillsdevelopment.app.server.data.model.Person;
import com.skillsdevelopment.app.web.resource.PersonResource;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class PersonRepresentation extends ResourceSupport {

    private String firstName;
    private String lastName;
    private String email;

    public PersonRepresentation(final Person person) {
        final String personId = person.getUuid().toString();
        this.add(
                linkTo(methodOn(PersonResource.class).getPerson(personId)).withSelfRel(),
                linkTo(methodOn(PersonResource.class).getAllPersons(null, null)).withRel("list"),
                linkTo(methodOn(PersonResource.class).getJobs(personId)).withRel("jobs"));
        firstName = person.getFirstName();
        lastName = person.getLastName();
        email = person.getEmail();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
