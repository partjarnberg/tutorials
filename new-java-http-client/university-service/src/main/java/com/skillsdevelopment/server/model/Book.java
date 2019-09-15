package com.skillsdevelopment.server.model;

public class Book {
    private final String title;
    private final String isbn;

    private Book(final String title, final String isbn) {
        this.title = title;
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getIsbn() {
        return isbn;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String title;
        private String isbn;

        public Builder setTitle(final String title) {
            this.title = title;
            return this;
        }

        public Builder setIsbn(final String isbn) {
            this.isbn = isbn;
            return this;
        }

        public Book build() {
            return new Book(title, isbn);
        }
    }
}
