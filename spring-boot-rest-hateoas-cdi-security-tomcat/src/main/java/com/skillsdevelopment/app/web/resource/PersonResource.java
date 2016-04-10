package com.skillsdevelopment.app.web.resource;

import com.google.common.eventbus.EventBus;
import com.skillsdevelopment.app.server.data.JobStore;
import com.skillsdevelopment.app.server.data.PersonStore;
import com.skillsdevelopment.app.server.data.model.Person;
import com.skillsdevelopment.app.server.event.PersonRetrievedEvent;
import com.skillsdevelopment.app.web.representation.JobsRepresentation;
import com.skillsdevelopment.app.web.representation.PersonLinksRepresentation;
import com.skillsdevelopment.app.web.representation.PersonRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/my-app/api/persons")
public class PersonResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonResource.class);

    @Autowired
    private PersonStore personStore;

    @Autowired
    private JobStore jobStore;

    @Autowired
    private EventBus eventBus;

    @PostConstruct
    private void init() {
        LOGGER.debug("Connected to event bus {}:{} ", eventBus, eventBus.hashCode());
    }

    @RequestMapping(method = GET)
    public HttpEntity<PersonLinksRepresentation> getAllPersons(@RequestParam(value = "offset", required = false) final Integer offset,
                                    @RequestParam(value = "limit", required = false) final Integer limit) {
        final PersonLinksRepresentation persons = new PersonLinksRepresentation(personStore.getAll(
                offset != null ? offset : 0,
                limit != null ? limit : 50), offset, limit);
        return new ResponseEntity<>(persons, HttpStatus.OK);
    }

    @RequestMapping(method = GET, value = "/{personId}")
    public HttpEntity<PersonRepresentation> getPerson(@PathVariable("personId") final String personId) {
        final Person person = personStore.get(UUID.fromString(personId));
        final PersonRepresentation personRepresentation = new PersonRepresentation(person);

        eventBus.post((PersonRetrievedEvent) () ->
                person.getFirstName() + " " + person.getLastName());
        return new ResponseEntity<>(personRepresentation, HttpStatus.OK);
    }

    @RequestMapping(method = GET, value = "/{personId}/jobs")
    public HttpEntity<JobsRepresentation> getJobs(@PathVariable("personId") final String personId) {
        return new ResponseEntity<>(new JobsRepresentation(personId, jobStore.getAll(UUID.fromString(personId))), HttpStatus.OK);
    }
}
