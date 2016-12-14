package com.skillsdevelopment;

import com.skillsdevelopment.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

@RunWith(WeldJUnit4Runner.class)
public class ServiceTest {

    @Inject
    private Service firstService;
    @Inject
    private Service secondService;

    @Test
    public void registerSameUserFromSameService() throws Exception {
        User firstUser = firstService.registerSameUser();
        User secondUser = firstService.registerSameUser();

        assertThat(firstUser, is(equalTo(secondUser)));
    }

    @Test
    public void registerSameUserDifferentService() throws Exception {
        User firstUser = firstService.registerSameUser();
        User secondUser = secondService.registerSameUser();

        assertThat(firstUser, is(equalTo(secondUser)));
    }

    @Test
    public void registerNewUserSameService() throws Exception {
        User firstUser = firstService.registerNewUser();
        User secondUser = firstService.registerNewUser();

        // Since the User has already been injected

        assertThat(firstUser, is(equalTo(secondUser)));
    }

    @Test
    public void registerNewUserDifferentService() throws Exception {
        User firstUser = firstService.registerNewUser();
        User secondUser = secondService.registerNewUser();

        assertThat(firstUser, is(not(equalTo(secondUser))));
    }
}