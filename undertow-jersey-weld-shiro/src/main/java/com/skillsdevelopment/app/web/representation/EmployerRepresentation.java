package com.skillsdevelopment.app.web.representation;

import com.skillsdevelopment.app.server.data.model.Employer;
import com.skillsdevelopment.app.web.adapter.LinkAdapter;
import com.skillsdevelopment.app.web.resource.EmployerResource;
import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@XmlRootElement
public class EmployerRepresentation {
    @InjectLinks({
            @InjectLink(
                    style = InjectLink.Style.ABSOLUTE,
                    resource = EmployerResource.class,
                    method = "getEmployer",
                    rel = "self",
                    bindings = {
                            @Binding(name = "employerId", value = "${instance.employerId}")
                    }
            )
    })
    @XmlJavaTypeAdapter(LinkAdapter.class)
    private List<Link> links;

    private String name;
    private String email;
    @XmlTransient
    private UUID employerId;

    public EmployerRepresentation() {
    }

    public EmployerRepresentation(final Employer employer) {
        this.name = employer.getName();
        this.email = employer.getEmail();
        this.employerId = employer.getUuid();
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public UUID getEmployerId() {
        return employerId;
    }
}
