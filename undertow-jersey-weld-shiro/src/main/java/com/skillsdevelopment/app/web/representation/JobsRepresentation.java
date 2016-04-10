package com.skillsdevelopment.app.web.representation;

import com.skillsdevelopment.app.server.data.model.Job;
import com.skillsdevelopment.app.web.adapter.LinkAdapter;
import com.skillsdevelopment.app.web.resource.PersonResource;
import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.*;

@XmlRootElement
public class JobsRepresentation {
    @InjectLinks({
            @InjectLink(
                    style = InjectLink.Style.ABSOLUTE,
                    resource = PersonResource.class,
                    method = "getJobs",
                    rel = "self",
                    bindings = {
                            @Binding(name = "personId", value = "${instance.personId}")
                    }
            )
    })
    @XmlJavaTypeAdapter(LinkAdapter.class)
    private List<Link> links;

    @XmlTransient
    private UUID personId;

    private Set<JobRepresentation> jobs = new HashSet<>();

    public JobsRepresentation() {
    }

    public JobsRepresentation(final UUID personId, final Set<Job> jobs) {
        this.personId = personId;
        jobs.forEach(job -> {
            this.jobs.add(new JobRepresentation(job.getProfession(), job.getEmployerId(), job.getEmployerName()));
        });
    }

    public Set<JobRepresentation> getJobs() {
        return jobs;
    }

    public void setJobs(final Set<JobRepresentation> jobs) {
        this.jobs = jobs;
    }

    public UUID getPersonId() {
        return personId;
    }
}
