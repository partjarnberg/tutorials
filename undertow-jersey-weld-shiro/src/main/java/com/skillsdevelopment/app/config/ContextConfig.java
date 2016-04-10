package com.skillsdevelopment.app.config;

import com.google.common.eventbus.EventBus;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

@ApplicationScoped
public class ContextConfig {

    @Produces @Singleton
    public EventBus eventBus() {
        return new EventBus();
    }
}
