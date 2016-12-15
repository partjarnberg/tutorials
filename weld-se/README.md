# Weld SE
## Using proxy for CDI
```java
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
```

```java
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
```
