package com.skillsdevelopment.app.web.representation;

import com.skillsdevelopment.app.server.data.model.Person;
import com.skillsdevelopment.app.web.resource.PersonResource;
import org.springframework.hateoas.ResourceSupport;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class PersonLinksRepresentation extends ResourceSupport {

    private Set<PersonLinkRepresentation> persons = new HashSet<>();

    public PersonLinksRepresentation(final Set<Person> persons, final Integer offset, final Integer limit) {
        this.add(linkTo(methodOn(PersonResource.class).getAllPersons(offset, limit)).withSelfRel());
        persons.forEach(person -> {
            final PersonLinkRepresentation personLink = new PersonLinkRepresentation(person.getFirstName(), person.getLastName());
            personLink.add(linkTo(methodOn(PersonResource.class).getPerson(person.getUuid().toString())).withSelfRel());
            this.persons.add(personLink);
        });
    }

    public Set<PersonLinkRepresentation> getPersons() {
        return persons;
    }
}
