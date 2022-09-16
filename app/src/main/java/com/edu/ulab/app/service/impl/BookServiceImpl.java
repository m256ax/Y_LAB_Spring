package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.generator.IdGenerator;
import com.edu.ulab.app.generator.impl.BookIdGeneratorImpl;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.storage.BookStorage;
import com.edu.ulab.app.storage.impl.BookStorageImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class BookServiceImpl implements BookService {

    private final IdGenerator idGenerator = new BookIdGeneratorImpl();

    private final BookStorage bookStorage = new BookStorageImpl();

    @Override
    public BookDto createBook(BookDto bookDto) {
        bookDto.setId(idGenerator.next());

        Book newBook = Book.builder()
                .id(bookDto.getId())
                .userId(bookDto.getUserId())
                .author(bookDto.getAuthor())
                .title(bookDto.getTitle())
                .pageCount(bookDto.getPageCount())
                .build();

        bookStorage.save(newBook);

        return bookDto;
    }

    @Override
    public BookDto updateBook(BookDto bookDto) {
        Optional<Book> optionalBook = bookStorage.findById(bookDto.getId());

        if(optionalBook.isPresent()) {
            Book existBook = optionalBook.get();

            existBook.setUserId(bookDto.getUserId());
            existBook.setTitle(bookDto.getTitle());
            existBook.setAuthor(bookDto.getAuthor());
            existBook.setPageCount(bookDto.getPageCount());

            bookStorage.update(existBook);

        }

        return bookDto;
    }

    @Override
    public BookDto getBookById(Long id) {
        Optional<Book> optionalBook = bookStorage.findById(id);

        BookDto bookDto = new BookDto();

        if(optionalBook.isPresent()){
            Book existBook = optionalBook.get();

            bookDto.setId(id);
            bookDto.setUserId(existBook.getUserId());
            bookDto.setTitle(existBook.getTitle());
            bookDto.setAuthor(existBook.getAuthor());
            bookDto.setPageCount(existBook.getPageCount());
        }

        return bookDto;
    }

    @Override
    public void deleteBookById(Long id) {
        bookStorage.delete(id);
    }
}
