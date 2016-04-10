package com.skillsdevelopment.app.web.resource;

import com.skillsdevelopment.app.server.data.EmployerStore;
import com.skillsdevelopment.app.web.representation.EmployerRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/my-app/api/employers")
public class EmployerResource {

    @Autowired
    private EmployerStore employerStore;

    @RequestMapping(method = GET, value = "/{employerId}")
    public HttpEntity<EmployerRepresentation> getEmployer(@PathVariable("employerId") final String employerId) {
        return new ResponseEntity<>(new EmployerRepresentation(employerStore.get(UUID.fromString(employerId))), HttpStatus.OK);
    }

}
