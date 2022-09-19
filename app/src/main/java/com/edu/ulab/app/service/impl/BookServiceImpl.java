package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.generator.IdGenerator;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.storage.BookStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BookServiceImpl implements BookService {

    @Autowired
    @Qualifier("BookId")
    private IdGenerator idGenerator;

    @Autowired
    private BookStorage bookStorage;

    @Autowired
    private BookMapper bookMapper;

    @Override
    public BookDto createBook(BookDto bookDto) {
        bookDto.setId(idGenerator.next());

        Book newBook = bookMapper.BookDtoToBook(bookDto);

        bookStorage.save(newBook);

        return bookDto;
    }

    @Override
    public void deleteBookById(Long id) {

        if (bookStorage.findById(id).isEmpty()) {
            throw new NotFoundException("This book is absent");
        }

        bookStorage.delete(id);
    }

    @Override
    public List<Long> getByUserId(Long userId) {
        return bookStorage.findByUserId(userId);
    }
}
