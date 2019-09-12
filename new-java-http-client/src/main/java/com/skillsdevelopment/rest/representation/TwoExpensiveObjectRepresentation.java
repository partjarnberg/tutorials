package com.skillsdevelopment.rest.representation;

public class TwoExpensiveObjectRepresentation {
    private final SomeExpensiveObjectRepresentation first;
    private final SomeExpensiveObjectRepresentation second;

    public TwoExpensiveObjectRepresentation(final SomeExpensiveObjectRepresentation first,
                                            final SomeExpensiveObjectRepresentation second) {
        this.first = first;
        this.second = second;
    }

    public SomeExpensiveObjectRepresentation getFirst() {
        return first;
    }

    public SomeExpensiveObjectRepresentation getSecond() {
        return second;
    }
}
