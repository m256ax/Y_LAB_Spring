package com.edu.ulab.app.storage.impl;

import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.storage.BookStorage;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class BookStorageImpl implements BookStorage {

    private final Map<Long, Book> bookMap = new HashMap<>();

    @Override
    public Optional<Book> findById(Long bookId) {
        return Optional.ofNullable(bookMap.get(bookId));
    }

    @Override
    public void delete(Long bookId) {
        bookMap.remove(bookId);
    }

    @Override
    public void save(Book book) {
        bookMap.put(book.getId(), book);
    }

    @Override
    public List<Long> findByUserId(Long userId) {
        return bookMap.entrySet().stream()
                .filter(entry -> Objects.equals(entry.getValue().getUserId(), userId))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
