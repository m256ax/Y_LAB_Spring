package com.edu.ulab.app.service;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.repository.BookRepository;
import com.edu.ulab.app.service.impl.BookServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Тестирование функционала {@link com.edu.ulab.app.service.impl.BookServiceImpl}.
 */
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DisplayName("Testing book functionality.")
public class BookServiceImplTest {
    @InjectMocks
    BookServiceImpl bookService;

    @Mock
    BookRepository bookRepository;

    @Mock
    BookMapper bookMapper;

    @Test
    @DisplayName("Создание книги. Должно пройти успешно.")
    void saveBook_Test() {
        //given
        Person person = new Person();
        person.setId(1L);

        BookDto bookDto = new BookDto();
        bookDto.setPersonId(1L);
        bookDto.setAuthor("test author");
        bookDto.setTitle("test title");
        bookDto.setPageCount(1000);

        BookDto result = new BookDto();
        result.setId(1L);
        result.setPersonId(1L);
        result.setAuthor("test author");
        result.setTitle("test title");
        result.setPageCount(1000);

        Book book = new Book();
        book.setPageCount(1000);
        book.setTitle("test title");
        book.setAuthor("test author");
        book.setPersonId(person.getId());

        Book savedBook = new Book();
        savedBook.setId(1L);
        savedBook.setPageCount(1000);
        savedBook.setTitle("test title");
        savedBook.setAuthor("test author");
        savedBook.setPersonId(person.getId());

        //when

        when(bookMapper.bookDtoToBook(bookDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(savedBook);
        when(bookMapper.bookToBookDto(savedBook)).thenReturn(result);

        //then
        BookDto bookDtoResult = bookService.createBook(bookDto);
        assertEquals(1L, bookDtoResult.getId());
    }

    // update
    @Test
    @DisplayName("Обновление книги. Должно пройти успешно.")
    void updateBook_Test() {
        //given
        Person person = new Person();
        person.setId(1L);

        BookDto bookDto = new BookDto();
        bookDto.setPersonId(1L);
        bookDto.setAuthor("new author");
        bookDto.setTitle("new title");
        bookDto.setPageCount(2222);

        BookDto result = new BookDto();
        result.setId(1L);
        result.setPersonId(1L);
        result.setAuthor("new author");
        result.setTitle("new title");
        result.setPageCount(2222);

        Book existBook = new Book();
        existBook.setPageCount(1000);
        existBook.setTitle("test title");
        existBook.setAuthor("test author");
        existBook.setPersonId(person.getId());

        Book updatedBook = new Book();
        updatedBook.setPageCount(2222);
        updatedBook.setTitle("new title");
        updatedBook.setAuthor("new author");
        updatedBook.setPersonId(person.getId());

        Book savedBook = new Book();
        savedBook.setId(1L);
        savedBook.setPageCount(2222);
        savedBook.setTitle("new title");
        savedBook.setAuthor("new author");
        savedBook.setPersonId(person.getId());

        //when

        when(bookRepository.findById(bookDto.getId())).thenReturn(Optional.of(existBook));
        when(bookMapper.bookDtoToBook(bookDto)).thenReturn(updatedBook);
        when(bookRepository.save(updatedBook)).thenReturn(savedBook);
        when(bookMapper.bookToBookDto(savedBook)).thenReturn(result);

        //then
        BookDto bookDtoResult = bookService.updateBook(bookDto);
        assertEquals(1L, bookDtoResult.getId());
        assertEquals(1L, bookDtoResult.getPersonId());
        assertEquals("new title", bookDtoResult.getTitle());
        assertEquals("new author", bookDtoResult.getAuthor());
        assertEquals(2222, bookDtoResult.getPageCount());
    }

    // get
    @Test
    @DisplayName("Получение книги. Должно пройти успешно.")
    void getBook_Test() {
        //given
        Person person = new Person();
        person.setId(1L);

        BookDto result = new BookDto();
        result.setId(1L);
        result.setPersonId(1L);
        result.setTitle("test title");
        result.setAuthor("test author");
        result.setPageCount(1000);

        Book existBook = new Book();
        existBook.setPersonId(person.getId());
        existBook.setTitle("test title");
        existBook.setAuthor("test author");
        existBook.setPageCount(1000);

        //when

        when(bookRepository.findById(1L)).thenReturn(Optional.of(existBook));
        when(bookMapper.bookToBookDto(existBook)).thenReturn(result);

        //then
        BookDto bookDtoResult = bookService.getBookById(1L);
        assertEquals(1L, bookDtoResult.getId());
        assertEquals(1L, bookDtoResult.getPersonId());
        assertEquals("test title", bookDtoResult.getTitle());
        assertEquals("test author", bookDtoResult.getAuthor());
        assertEquals(1000, bookDtoResult.getPageCount());
    }

    // get all
    @Test
    @DisplayName("Получение всех книг. Должно пройти успешно.")
    void getAllBook_Test() {
        //given
        Person person = new Person();
        person.setId(1L);

        List<Long> testResult = new ArrayList<>();
        testResult.add(1L);

        Book existBook = new Book();
        existBook.setPersonId(person.getId());
        existBook.setTitle("test title");
        existBook.setAuthor("test author");
        existBook.setPageCount(1000);

        //when

        when(bookRepository.findByPersonId(1L)).thenReturn(Optional.of(Collections.singletonList(existBook)));
        when(bookMapper.createListId(Optional.of(Collections.singletonList(existBook)))).thenReturn(List.of(1L));

        //then
        List<Long> result = bookService.getBookByUserId(1L);
        assertEquals(testResult.get(0), result.get(0));
    }

    // delete
    @Test
    @DisplayName("Удаление книги. Должно пройти успешно.")
    void deleteBook_Test() {
        //given
        Person person = new Person();
        person.setId(1L);

        Book existBook = new Book();
        existBook.setPersonId(person.getId());
        existBook.setTitle("test title");
        existBook.setAuthor("test author");
        existBook.setPageCount(1000);

        //when

        when(bookRepository.findById(1L)).thenReturn(Optional.of(existBook));
        bookRepository.delete(existBook);

        //then
        verify(bookRepository).delete(existBook);
    }

    // * failed
    @Test
    @DisplayName("Ошибка при получении книги. Должно не пройти успешно.")
    void failToGetPerson_Test() {
        //given

        Book book = new Book();
        book.setId(5L);

        //when
        Throwable exception = assertThrows(NotFoundException.class, () -> bookService.getBookById(book.getId()));

        //then
        assertEquals("This book is absent", exception.getMessage());
    }

    @Test
    @DisplayName("Ошибка при обновлении книги. Должно не пройти успешно.")
    void failToUpdatePerson_Test() {
        //given

        BookDto bookDto = new BookDto();
        bookDto.setId(5L);

        //when
        Throwable exception = assertThrows(NotFoundException.class, () -> bookService.updateBook(bookDto));

        //then
        assertEquals("This book is absent", exception.getMessage());
    }

    @Test
    @DisplayName("Ошибка при удалении книги. Должно не пройти успешно.")
    void failToDeletePerson_Test() {
        //given

        BookDto bookDto = new BookDto();
        bookDto.setId(5L);

        //when
        Throwable exception = assertThrows(NotFoundException.class, () -> bookService.deleteBookById(bookDto.getId()));

        //then
        assertEquals("This book is absent", exception.getMessage());
    }
}
