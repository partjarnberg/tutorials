package com.skillsdevelopment.server.model;

import java.util.UUID;

public class MyObject {
    private final UUID id;
    private final String fanzyName;

    public MyObject(String fanzyName) {
        this.fanzyName = fanzyName;
        id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public String getFanzyName() {
        return fanzyName;
    }
}
