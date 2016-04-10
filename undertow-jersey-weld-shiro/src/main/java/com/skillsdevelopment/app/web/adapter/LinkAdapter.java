package com.skillsdevelopment.app.web.adapter;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.namespace.QName;
import java.util.HashMap;
import java.util.Map;

public class LinkAdapter extends XmlAdapter<LinkJaxb, Link> {
    @Override
    public Link unmarshal(LinkJaxb jaxbLink) throws Exception {
        Link.Builder builder = Link.fromUri(jaxbLink.getUri());
        for (Map.Entry<QName, Object> entry : jaxbLink.getParams().entrySet()) {
            builder.param(entry.getKey().getLocalPart(), entry.getValue().toString());
        }
        return builder.build();
    }

    @Override
    public LinkJaxb marshal(Link link) throws Exception {
        Map<QName, Object> params = new HashMap<>();
        for (Map.Entry<String,String> entry : link.getParams().entrySet()) {
            params.put(new QName("", entry.getKey()), entry.getValue());
        }
        return new LinkJaxb(link.getUri(), params);
    }
}
