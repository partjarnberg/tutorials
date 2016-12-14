package com.skillsdevelopment;

import com.skillsdevelopment.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;

public class SecondService {
    private final static Logger LOGGER = LoggerFactory.getLogger(SecondService.class);

    public User createUser() {
        final User user = CDI.current().select(User.class).get();
        LOGGER.info("Registering user {}", user);
        return user;
    }
}
