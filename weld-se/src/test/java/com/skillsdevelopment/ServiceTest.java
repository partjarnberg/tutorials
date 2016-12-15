package com.skillsdevelopment;

import com.skillsdevelopment.model.User;
import com.skillsdevelopment.util.WeldJUnit4Runner;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

@RunWith(WeldJUnit4Runner.class)
public class ServiceTest {

    @Inject
    private Service service;

    @Inject
    private Instance<Service> services;

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

    @Test
    public void recycleUserMultipleServicesTest() throws Exception {
        final Service firstService = services.get();
        final Service secondService = services.get();
        assertThat(firstService, is(not(equalTo(secondService))));

        final User firstUser = firstService.recycleUser();
        final User secondUser = secondService.recycleUser();
        assertThat(firstUser, is(equalTo(secondUser)));
    }
}