package com.skillsdevelopment;

import com.skillsdevelopment.model.User;
import com.skillsdevelopment.util.WeldJUnit4Runner;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

@RunWith(WeldJUnit4Runner.class)
public class FirstServiceTest {

    @Inject
    private FirstService service;


    @Test
    public void createUserTest() throws Exception {
        User firstUser = service.createUser();
        User secondUser = service.createUser();

        assertThat(firstUser, is(not(equalTo(secondUser))));
    }
}