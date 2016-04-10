package com.skillsdevelopment.app.web.representation;

import com.skillsdevelopment.app.web.adapter.LinkAdapter;
import com.skillsdevelopment.app.web.resource.EmployerResource;
import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;
import java.util.UUID;

public class JobRepresentation {
    @InjectLinks({
            @InjectLink(
                    style = InjectLink.Style.ABSOLUTE,
                    resource = EmployerResource.class,
                    method = "getEmployer",
                    rel = "employer",
                    bindings = {
                            @Binding(name = "employerId", value = "${instance.employerId}")
                    }
            )
    })
    @XmlJavaTypeAdapter(LinkAdapter.class)
    private List<Link> links;

    private String profession;
    private String employerName;
    @XmlTransient
    private UUID employerId;

    public JobRepresentation() {
    }

    public JobRepresentation(final String profession, final String employerName) {
        this.profession = profession;
        this.employerName = employerName;
    }

    public JobRepresentation(final String profession, final UUID employerId, final String employerName) {
        this.profession = profession;
        this.employerId = employerId;
        this.employerName = employerName;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(final String profession) {
        this.profession = profession;
    }

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(final String employerName) {
        this.employerName = employerName;
    }

    public UUID getEmployerId() {
        return employerId;
    }
}
