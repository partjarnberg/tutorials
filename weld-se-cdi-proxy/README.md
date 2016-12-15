# Weld SE
## Using proxy for CDI
```java
public class UserFactory {

    @Produces @Create
    public User create() {
        return new User();
    }

    @ApplicationScoped
    @Produces @Recycle
    public User recycle() {
        return new User();
    }

}
```

Then do the following to inject users:
```java
public class Service {

    @Inject @Create
    private Provider<User> userFactory;

    @Inject @Recycle
    private Provider<User> userRecycler;

    public User createUser() {
        return userFactory.get();
    }

    public User recycleUser() {
        return userRecycler.get();
    }
}
```

Running tests:
```java
@Test
public void createUserTest() throws Exception {
    final User firstUser = service.createUser();
    final User secondUser = service.createUser();
    assertThat(firstUser, is(not(equalTo(secondUser))));
}

@Test
public void recycleUserTest() throws Exception {
    final User firstUser = service.recycleUser();
    final User secondUser = service.recycleUser();
    assertThat(firstUser, is(equalTo(secondUser)));
}
```