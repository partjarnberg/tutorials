package com.skillsdevelopment.rest.representation;

public class SomeExpensiveObjectRepresentation {
    private final String id;
    private final String name;

    public SomeExpensiveObjectRepresentation(final String id, final String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
