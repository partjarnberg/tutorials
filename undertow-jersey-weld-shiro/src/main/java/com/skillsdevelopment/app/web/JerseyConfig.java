package com.skillsdevelopment.app.web;

import com.skillsdevelopment.app.web.filter.UTF8ResponseFilter;
import org.glassfish.jersey.linking.DeclarativeLinkingFeature;
import org.glassfish.jersey.moxy.json.MoxyJsonConfig;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
import org.glassfish.jersey.moxy.xml.MoxyXmlFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ext.ContextResolver;

public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        packages(true, JerseyConfig.class.getPackage().getName());
        register(MoxyJsonFeature.class);
        register(MoxyXmlFeature.class);
        register(UTF8ResponseFilter.class);
        register(DeclarativeLinkingFeature.class);
        register(createMoxyJsonResolver());
    }

    private ContextResolver<MoxyJsonConfig> createMoxyJsonResolver() {
        final MoxyJsonConfig moxyJsonConfig = new MoxyJsonConfig();
        moxyJsonConfig.setFormattedOutput(true);
        return moxyJsonConfig.resolver();
    }
}
