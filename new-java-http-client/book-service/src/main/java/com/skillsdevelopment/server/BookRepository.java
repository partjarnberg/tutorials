package com.skillsdevelopment.server;

import java.util.Map;
import java.util.UUID;

public interface BookRepository {
    Book get(final UUID id);
    Map<UUID, Book> getAllMatching(final String titleQuery);
}
