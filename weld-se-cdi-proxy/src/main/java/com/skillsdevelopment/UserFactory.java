package com.skillsdevelopment;

import com.skillsdevelopment.model.User;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

public class UserFactory {

    /**
     * This factory method will create a new {@link User} when injecting in the following way:
     * <pre>
     * &#064;Inject &#064;Create
     * private Provider<User> userFactory;
     * </pre>
     * @return a new {@link User}
     */
    @Produces @Create
    public User create() {
        return new User();
    }

    /**
     * This factory method will recycle {@link User} over the application scope:
     * <pre>
     * &#064;Inject &#064;Reuse
     * private Provider<User> userFactory;
     * </pre>
     * @return a reused {@link User}
     */
    @ApplicationScoped
    @Produces @Recycle
    public User recycle() {
        return new User();
    }

}
