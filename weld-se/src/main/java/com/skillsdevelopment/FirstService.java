package com.skillsdevelopment;

import com.skillsdevelopment.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;

public class FirstService {
    private final static Logger LOGGER = LoggerFactory.getLogger(FirstService.class);

    @Inject
    private Provider<User> userProvider;

    public User createUser() {
        final User user = userProvider.get();
        LOGGER.info("Registering user {}", user);
        return user;
    }
}
