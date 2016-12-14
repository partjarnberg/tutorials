package com.skillsdevelopment;

import com.skillsdevelopment.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class Service {

    private final static Logger LOGGER = LoggerFactory.getLogger(Service.class);

    @Inject @NewUser
    private User newUser;

    @Inject @SameUser
    private User sameUser;

    public User registerNewUser() {
        LOGGER.info("Registering user {}", newUser);
        return newUser;
    }

    public User registerSameUser() {
        LOGGER.info("Registering user {}", sameUser);
        return sameUser;
    }
}
