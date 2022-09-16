package com.edu.ulab.app.storage.impl;

import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.storage.BookStorage;

import java.util.*;

public class BookStorageImpl implements BookStorage {

    private final Map<Long, Book> bookMap = new HashMap<>();

    @Override
    public void update(Book book) {
        bookMap.replace(book.getId(), book);
    }

    @Override
    public Optional<Book> findById(Long id) {
        return Optional.of(bookMap.get(id));
    }

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(bookMap.values());
    }

    @Override
    public void delete(Long id) {
        bookMap.remove(id);
    }

    @Override
    public void save(Book book) {
        bookMap.put(book.getId(), book);
    }

}
