package com.skillsdevelopment.app.server;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.skillsdevelopment.app.config.annotation.Eager;
import com.skillsdevelopment.app.server.event.PersonRetrievedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped @Eager
public class EventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventListener.class);

    @Inject
    private EventBus eventBus;

    @PostConstruct
    private void init() {
        LOGGER.debug("Register to event bus {}:{} ", eventBus, eventBus.hashCode());
        eventBus.register(this);
    }

    @Subscribe
    public void handlePersonRetrievedEvent(final PersonRetrievedEvent event) {
        LOGGER.info("Full name of person being requested: {}", event.fullName());
    }
}
