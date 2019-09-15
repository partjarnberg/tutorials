package com.skillsdevelopment.server;

import java.util.Map;
import java.util.UUID;

public class BookService {
    private final BookRepository repository;

    public BookService(final BookRepository repository) {
        this.repository = repository;
    }

    public Book get(final UUID id) {
        return repository.get(id);
    }

    public Map<UUID, Book> query(final String query) {
        return repository.getAllMatching(query);
    }
}
