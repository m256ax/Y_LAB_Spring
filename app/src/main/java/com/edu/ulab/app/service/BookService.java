package com.edu.ulab.app.service;


import com.edu.ulab.app.dto.BookDto;

import java.util.List;

public interface BookService {
    BookDto createBook(BookDto userDto);

    void deleteBookById(Long id);

    List<Long> getByUserId(Long userId);

}
