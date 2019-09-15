package com.skillsdevelopment.rest.representation;

import com.skillsdevelopment.server.Book;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class BooksRepresentation {
    private final Set<BookRepresentation> books;

    public BooksRepresentation(final Map<UUID, Book> books) {
        this.books = books.entrySet().stream().map(entry ->
                new BookRepresentation(entry.getKey(), entry.getValue()))
                .collect(Collectors.toSet());
    }

    public Set<BookRepresentation> getBooks() {
        return books;
    }
}
