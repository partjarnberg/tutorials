package com.skillsdevelopment.app.web.resource;

import com.google.common.collect.ImmutableMap;
import com.google.common.eventbus.EventBus;
import com.skillsdevelopment.app.server.data.JobStore;
import com.skillsdevelopment.app.server.data.PersonStore;
import com.skillsdevelopment.app.server.data.model.Job;
import com.skillsdevelopment.app.server.data.model.Person;
import com.skillsdevelopment.app.server.event.PersonRetrievedEvent;
import com.skillsdevelopment.app.web.representation.JobsRepresentation;
import com.skillsdevelopment.app.web.representation.PersonLinksRepresentation;
import com.skillsdevelopment.app.web.representation.PersonRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@Path("persons")
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class PersonResource implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonResource.class);

    @Inject
    private PersonStore personStore;

    @Inject
    private JobStore jobStore;

    @Inject
    private EventBus eventBus;

    @PostConstruct
    private void init() {
        LOGGER.debug("Connected to event bus {}:{} ", eventBus, eventBus.hashCode());
    }

    @GET
    public Response getAllPersons(@QueryParam("offset") final Integer offset, @QueryParam("limit") final Integer limit) {
        return Response.ok(new PersonLinksRepresentation(
                personStore.getAll(
                        offset != null ? offset : 0,
                        limit != null ? limit : 50))).build();
    }

    @Path("{personId}")
    @GET
    public Response getPerson(@PathParam("personId") final String personId) {
        final Person person;
        try {
            person = personStore.get(UUID.fromString(personId));
        } catch (IllegalArgumentException ignore) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if(person == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        eventBus.post((PersonRetrievedEvent) () ->
                person.getFirstName() + " " + person.getLastName());

        return Response.ok(new PersonRepresentation(person)).build();
    }

    @Path("{personId}/jobs")
    @GET
    public Response getJobs(@PathParam("personId") final String personId) {
        try {
            return Response.ok(new JobsRepresentation(UUID.fromString(personId), jobStore.getAll(UUID.fromString(personId)))).build();
        } catch (IllegalArgumentException ignore) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
