package com.edu.ulab.app.repository;

import com.edu.ulab.app.config.SystemJpaTest;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.Person;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static com.vladmihalcea.sql.SQLStatementCountValidator.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тесты репозитория {@link BookRepository}.
 */
@SystemJpaTest
public class BookRepositoryTest {
    @Autowired
    BookRepository bookRepository;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        SQLStatementCountValidator.reset();
    }

    @DisplayName("Сохранить книгу и автора. Число insert должно равняться 2")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void saveBook_thenAssertDmlCount() {
        //Given
        Person person = new Person();
        person.setAge(111);
        person.setTitle("writer");
        person.setFullName("Test Test");

        Person savedPerson = userRepository.save(person);

        Book book = new Book();
        book.setAuthor("Test Author");
        book.setTitle("test");
        book.setPageCount(1000);
        book.setPersonId(savedPerson.getId());

        //When
        Book result = bookRepository.save(book);

        //Then
        assertThat(bookRepository.count()).isEqualTo(3);
        assertThat(result.getPageCount()).isEqualTo(1000);
        assertThat(result.getTitle()).isEqualTo("test");
        assertSelectCount(1);
        assertInsertCount(2);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    // update
    @DisplayName("Обновить книгу, автора, количество страниц. Число update должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void updateBook_thenAssertDmlCount() {
        //Given
        Person person = new Person();
        person.setAge(111);
        person.setTitle("writer");
        person.setFullName("Test Test");

        Person savedPerson = userRepository.save(person);

        Book book = new Book();
        book.setAuthor("Test Author");
        book.setTitle("test");
        book.setPageCount(1000);
        book.setPersonId(savedPerson.getId());

        Book savedBook = bookRepository.save(book);

        Book updateBook = new Book();
        updateBook.setAuthor("new Author");
        updateBook.setTitle("new test");
        updateBook.setPageCount(2222);
        updateBook.setPersonId(savedPerson.getId());

        //When
        Book existBook = bookRepository.findById(savedBook.getId()).get();
        existBook.setTitle(updateBook.getTitle());
        existBook.setAuthor(updateBook.getAuthor());
        existBook.setPageCount(updateBook.getPageCount());
        Book result = bookRepository.save(existBook);

        //Then
        assertThat(bookRepository.count()).isEqualTo(3);
        assertThat(result.getPageCount()).isEqualTo(2222);
        assertThat(result.getAuthor()).isEqualTo("new Author");
        assertThat(result.getTitle()).isEqualTo("new test");
        assertSelectCount(3);
        assertInsertCount(2);
        assertUpdateCount(1);
        assertDeleteCount(0);
    }

    // get
    @DisplayName("Получить книгу и автора. Число select должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void getBook_thenAssertDmlCount() {
        //Given
        Person person = new Person();
        person.setAge(111);
        person.setTitle("writer");
        person.setFullName("Test Test");

        Person savedPerson = userRepository.save(person);

        Book book = new Book();
        book.setAuthor("Test Author");
        book.setTitle("test");
        book.setPageCount(1000);
        book.setPersonId(savedPerson.getId());

        bookRepository.save(book);

        //When
        Book result = bookRepository.findById(book.getId()).get();

        //Then
        assertThat(bookRepository.count()).isEqualTo(3);
        assertThat(result.getPageCount()).isEqualTo(1000);
        assertThat(result.getAuthor()).isEqualTo("Test Author");
        assertThat(result.getTitle()).isEqualTo("test");
        assertSelectCount(1);
        assertInsertCount(2);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    // get all

    @DisplayName("Получить все книги. Число select должно равняться 2")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void getAllBook_thenAssertDmlCount() {
        //Given
        Person person = new Person();
        person.setAge(111);
        person.setTitle("writer");
        person.setFullName("Test Test");

        Person savedPerson = userRepository.save(person);

        Book book = new Book();
        book.setAuthor("Test Author");
        book.setTitle("test");
        book.setPageCount(1000);
        book.setPersonId(savedPerson.getId());

        bookRepository.save(book);

        //When
        List<Book> result = (List<Book>) bookRepository.findAll();

        //Then
        assertThat(bookRepository.count()).isEqualTo(3);
        assertThat(result.size()).isEqualTo(3);
        assertSelectCount(2);
        assertInsertCount(2);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    // delete

    @DisplayName("Удалить книгу. Число delete должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void deleteBook_thenAssertDmlCount() {
        //Given
        Person person = new Person();
        person.setAge(111);
        person.setTitle("writer");
        person.setFullName("Test Test");

        Person savedPerson = userRepository.save(person);

        Book book = new Book();
        book.setAuthor("Test Author");
        book.setTitle("test");
        book.setPageCount(1000);
        book.setPersonId(savedPerson.getId());

        bookRepository.save(book);

        //When
        bookRepository.delete(book);

        //Then
        assertThat(userRepository.count()).isEqualTo(2L);
        assertSelectCount(1);
        assertInsertCount(2);
        assertUpdateCount(0);
        assertDeleteCount(1);
    }

    // * failed
    @DisplayName("Ошибка при сохранении книги.")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })

    void failSaveBook_thenAssertDmlCount() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> bookRepository.save(null), "IO error");
    }
    @DisplayName("Ошибка при получении книги.")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })

    void failGetBook_thenAssertDmlCount() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> bookRepository.findById(null), "IO error");
    }
    @DisplayName("Ошибка при удалении книги.")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void failDeleteBook_thenAssertDmlCount() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> bookRepository.delete(null), "IO error");
    }

}
