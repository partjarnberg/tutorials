package com.skillsdevelopment.app.web.resource;

import com.skillsdevelopment.app.server.data.EmployerStore;
import com.skillsdevelopment.app.web.representation.EmployerRepresentation;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.UUID;

@Path("employers")
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class EmployerResource implements Serializable {

    @Inject
    private EmployerStore employerStore;

    @Path("{employerId}")
    @GET
    public Response getEmployer(@PathParam("employerId") final String employerId) {
        return Response.ok(new EmployerRepresentation(employerStore.get(UUID.fromString(employerId)))).build();
    }
}
