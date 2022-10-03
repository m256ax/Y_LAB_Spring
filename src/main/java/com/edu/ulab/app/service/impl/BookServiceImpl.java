package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.repository.BookRepository;
import com.edu.ulab.app.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    public BookServiceImpl(BookRepository bookRepository,
                           BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    public BookDto createBook(BookDto bookDto) {
        Book book = bookMapper.bookDtoToBook(bookDto);
        log.info("Mapped book: {}", book);
        Book savedBook = bookRepository.save(book);
        log.info("Saved book: {}", savedBook);
        return bookMapper.bookToBookDto(savedBook);
    }

    @Override
    public BookDto updateBook(BookDto bookDto) {
        // реализовать недостающие методы

        Optional<Book> optional = bookRepository.findById(bookDto.getId());

        nullChecker(optional);

        Book existBook = optional.get();
        log.info("Got book from storage: {}", existBook);
        existBook = bookMapper.bookDtoToBook(bookDto);
        log.info("Mapped book: {}", existBook);

        Book savedBook = bookRepository.save(existBook);
        log.info("Save updated book to storage: {}", existBook);

        return bookMapper.bookToBookDto(savedBook);
    }

    @Override
    public BookDto getBookById(Long bookId) {
        // реализовать недостающие методы
        log.info("Got bookId for delete: {}", bookId);

        Optional<Book> optional = bookRepository.findById(bookId);

        nullChecker(optional);

        BookDto bookDto = bookMapper.bookToBookDto(optional.get());
        log.info("Mapped bookDto: {}", bookDto);

        return bookDto;
    }

    private static void nullChecker(Optional<Book> optional) {
        if (optional.isEmpty()) {
            log.info("This bookId is absent in storage");
            throw new NotFoundException("This book is absent");
        }
    }

    @Override
    public void deleteBookById(Long bookId) {
        // реализовать недостающие методы
        log.info("Got bookId for delete: {}", bookId);

        Optional<Book> optional = bookRepository.findById(bookId);

        nullChecker(optional);

        bookRepository.delete(optional.get());
        log.info("Book with bookId: {} is deleted from storage", bookId);
    }

    @Transactional
    @Override
    public List<Long> getBookByUserId(Long userId) {
        log.info("Got userId for find book: {}", userId);

        Optional<List<Book>> optional = bookRepository.findByPersonId(userId);

        if (optional.isEmpty()) {
            log.info("Book with userid: {} is absent in storage", userId);
            throw new NotFoundException("This book is absent");
        }

        return bookMapper.createListId(optional);
    }
}
