package com.skillsdevelopment.rest.representation;

import java.util.List;

public class MyObjectListRepresentation {
    private final List<MyObjectRepresentation> myObjects;

    public MyObjectListRepresentation(final List<MyObjectRepresentation> myObjects) {
        this.myObjects = myObjects;
    }

    public List<MyObjectRepresentation> getMyObjects() {
        return myObjects;
    }
}
