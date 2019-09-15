package com.skillsdevelopment.server;

public class Book {
    private final String isbn;
    private final String title;
    private final String description;
    private final String imageName;

    private Book(final String isbn, final String title, final String description, final String imageName) {
        this.isbn = isbn;
        this.title = title;
        this.description = description;
        this.imageName = imageName;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageName() {
        return imageName;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String isbn;
        private String title;
        private String description;
        private String imageName;

        public Builder isbn(final String isbn) {
            this.isbn = isbn;
            return this;
        }

        public Builder title(final String title) {
            this.title = title;
            return this;
        }

        public Builder description(final String description) {
            this.description = description;
            return this;
        }

        public Builder imageName(final String imageName) {
            this.imageName = imageName;
            return this;
        }

        public Book build() {
            return new Book(isbn, title, description, imageName);
        }
    }
}
