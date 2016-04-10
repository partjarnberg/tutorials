package com.skillsdevelopment.app.web.filter;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

/**
 * Filter for applying UTF-8 for all responses via the rest interface.
 */
public class UTF8ResponseFilter implements ContainerResponseFilter {
    private static final Map<String, String> CHARSET_UTF8 = Collections.singletonMap("charset", "UTF-8");

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        MediaType oldType = responseContext.getMediaType();
        if(oldType != null) {
            MediaType newType = new MediaType(oldType.getType(), oldType.getSubtype(), CHARSET_UTF8);
            responseContext.getHeaders().putSingle(HttpHeaders.CONTENT_TYPE, newType);
        }
    }
}
