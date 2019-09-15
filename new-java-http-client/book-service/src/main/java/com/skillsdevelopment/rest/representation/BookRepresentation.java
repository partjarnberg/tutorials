package com.skillsdevelopment.rest.representation;

import com.skillsdevelopment.server.Book;

import java.util.UUID;

public class BookRepresentation {

    private final UUID id;
    private final Book book;

    public BookRepresentation(final UUID id, final Book book) {
        this.id = id;
        this.book = book;
    }

    public UUID getId() {
        return id;
    }

    public String getIsbn() {
        return book.getIsbn();
    }

    public String getDescription() {
        return book.getDescription();
    }

    public String getTitle() {
        return book.getTitle();
    }

    public String getImageUrl() {
        return "/static/img/" + book.getImageName();
    }
}
