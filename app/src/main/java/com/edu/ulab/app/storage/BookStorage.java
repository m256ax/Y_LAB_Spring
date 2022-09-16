package com.edu.ulab.app.storage;

import com.edu.ulab.app.entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookStorage {

    void update(Book book);

    Optional<Book> findById(Long id);

    List<Book> findAll();

    void delete(Long id);

    void save(Book book);




}
