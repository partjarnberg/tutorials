package com.skillsdevelopment.app.web.representation;

import com.skillsdevelopment.app.web.adapter.LinkAdapter;
import com.skillsdevelopment.app.web.resource.PersonResource;
import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.net.URI;
import java.util.List;
import java.util.UUID;

public class PersonLinkRepresentation {
    @InjectLinks({
            @InjectLink(
                    style = InjectLink.Style.ABSOLUTE,
                    resource = PersonResource.class,
                    method = "getPerson",
                    rel = "self",
                    bindings = {
                            @Binding(name = "personId", value = "${instance.uuid}")
                    }
            )
    })
    @XmlJavaTypeAdapter(LinkAdapter.class)
    private List<Link> links;

    @XmlTransient
    private UUID uuid;
    private String firstName;
    private String lastName;

    public PersonLinkRepresentation() {
    }

    public PersonLinkRepresentation(final UUID uuid, final String firstName, final String lastName) {
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public UUID getUuid() {
        return uuid;
    }
}
