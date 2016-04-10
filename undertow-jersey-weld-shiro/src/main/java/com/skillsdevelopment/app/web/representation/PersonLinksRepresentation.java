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
import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@XmlRootElement
public class PersonLinksRepresentation {
    private static final int MAX_LIMIT = 50;

    @InjectLinks({
            @InjectLink(
                    style = InjectLink.Style.ABSOLUTE,
                    resource = PersonResource.class,
                    method = "getAllPersons",
                    rel = "self",
                    bindings = {
                            @Binding(name = "offset", value = "${instance.offset}"),
                            @Binding(name = "limit", value = "${instance.limit}")
                    }
            )
    })
    @XmlJavaTypeAdapter(LinkAdapter.class)
    private List<Link> links;

    private Set<PersonLinkRepresentation> persons;

    @XmlTransient
    private int offset, limit;

    public PersonLinksRepresentation() {
        this(new HashSet<>());
    }

    public PersonLinksRepresentation(final Set<Person> persons) {
        this(0, MAX_LIMIT, persons);
    }

    public PersonLinksRepresentation(final int offset, final int limit,
                                     final Set<Person> persons) {
        this.offset = offset >= 0 ? offset : 0;
        this.limit = limit <= MAX_LIMIT ? limit : MAX_LIMIT;
        this.persons = new HashSet<>();
        persons.forEach(p -> this.persons.add(new PersonLinkRepresentation(p.getUuid(), p.getFirstName(), p.getLastName())));
    }

    public Set<PersonLinkRepresentation> getPersons() {
        return persons;
    }

    public void setPersons(final Set<PersonLinkRepresentation> persons) {
        this.persons = persons;
    }

    public int getOffset() {
        return offset;
    }

    public int getLimit() {
        return limit;
    }
}
