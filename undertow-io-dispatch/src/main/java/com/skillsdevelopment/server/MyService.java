package com.skillsdevelopment.server;

import com.skillsdevelopment.server.model.MyObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MyService {

    private Map<UUID, MyObject> myObjectsStorage;

    public MyService() {
        myObjectsStorage = Stream.of(
                new MyObject("first"),
                new MyObject("second"),
                new MyObject("third"),
                new MyObject("fourth"),
                new MyObject("fift"),
                new MyObject("sixth"),
                new MyObject("seventh"),
                new MyObject("eigth"),
                new MyObject("nineth"),
                new MyObject("tenth"))
                .collect(Collectors.toMap(MyObject::getId, data -> data));
    }

    public Optional<MyObject> findObject(final UUID id) {
        return Optional.ofNullable(myObjectsStorage.get(id));
    }

    public List<MyObject> findAll() {
        return new ArrayList<>(myObjectsStorage.values());
    }

    public MyObject createObject(final String name) {
        final MyObject myObject = new MyObject(name);
        myObjectsStorage.put(myObject.getId(), myObject);
        return myObject;
    }
}
