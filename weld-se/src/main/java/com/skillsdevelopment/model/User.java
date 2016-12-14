package com.skillsdevelopment.model;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class User {
    private static final AtomicInteger COUNTER = new AtomicInteger(1);
    private final String id;

    public User() {
        id = "user-" + COUNTER.getAndIncrement();
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
