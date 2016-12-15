package com.skillsdevelopment;

import com.skillsdevelopment.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;

public class Service {
    private final static Logger LOGGER = LoggerFactory.getLogger(Service.class);

    @Inject @Create
    private Provider<User> userFactory;

    @Inject @Recycle
    private Provider<User> userRecycler;

    public User createUser() {
        final User user = userFactory.get();
        LOGGER.info("Registering user {}", user);
        return user;
    }

    public User recycleUser() {
        final User user = userRecycler.get();
        LOGGER.info("Registering user {}", user);
        return user;
    }
}
