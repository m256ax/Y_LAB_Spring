package com.edu.ulab.app.storage;

import com.edu.ulab.app.entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookStorage {
    
    Optional<Book> findById(Long id);

    void delete(Long id);

    void save(Book book);

    List<Long> findByUserId(Long id);

}
