package com.skillsdevelopment;

import com.skillsdevelopment.model.User;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Produces;

public class UserFactory {

    @Produces @Any
    public User create() {
        return new User();
    }

}
