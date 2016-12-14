package com.skillsdevelopment;

import com.skillsdevelopment.model.User;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

public class UserProducer {

    @SameUser
    @Produces @ApplicationScoped
    public User reuse() {
        return new User();
    }

    @NewUser
    @Produces
    public User create() {
        return new User();
    }
}
