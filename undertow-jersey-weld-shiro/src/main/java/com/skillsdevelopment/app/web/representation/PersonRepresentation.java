package com.skillsdevelopment.app.web.representation;

import com.skillsdevelopment.app.server.data.model.Person;
import com.skillsdevelopment.app.web.adapter.LinkAdapter;
import com.skillsdevelopment.app.web.resource.PersonResource;
import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;
import java.util.UUID;

@XmlRootElement
public class PersonRepresentation {
    @InjectLinks({
            @InjectLink(
                    style = InjectLink.Style.ABSOLUTE,
                    resource = PersonResource.class,
                    method = "getPerson",
                    rel = "self",
                    bindings = {
                            @Binding(name = "personId", value = "${instance.uuid}")
                    }
            ),
            @InjectLink(
                    style = InjectLink.Style.ABSOLUTE,
                    resource = PersonResource.class,
                    method = "getAllPersons",
                    rel = "list",
                    bindings = {
                            @Binding(name = "offset", value = "0"),
                            @Binding(name = "limit", value = "50")
                    }
            ),
            @InjectLink(
                    style = InjectLink.Style.ABSOLUTE,
                    resource = PersonResource.class,
                    method = "getJobs",
                    rel = "jobs",
                    bindings = {
                            @Binding(name = "personId", value = "${instance.uuid}")
                    }
            )
    })
    @XmlJavaTypeAdapter(LinkAdapter.class)
    private List<Link> links;

    @XmlTransient
    private UUID uuid;
    private String email;
    private String firstName;
    private String lastName;

    public PersonRepresentation() {
    }

    public PersonRepresentation(final Person person) {
        uuid = person.getUuid();
        email = person.getEmail();
        firstName = person.getFirstName();
        lastName = person.getLastName();
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PersonRepresentation that = (PersonRepresentation) o;

        return email != null ? email.equals(that.email) : that.email == null;

    }

    @Override
    public int hashCode() {
        return email != null ? email.hashCode() : 0;
    }
}
