package com.skillsdevelopment.app.web.representation;

import org.springframework.hateoas.ResourceSupport;

public class PersonLinkRepresentation extends ResourceSupport {
    private String firstName;
    private String lastName;

    public PersonLinkRepresentation(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
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
}
