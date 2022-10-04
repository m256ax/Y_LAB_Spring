package com.edu.ulab.app.service;


import com.edu.ulab.app.dto.BookDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookService {
    BookDto createBook(BookDto bookDto);

    BookDto updateBook(BookDto bookDto);

    BookDto getBookById(Long id);

    void deleteBookById(Long id);

    List<Long> getBookByUserId(Long userId);

}
