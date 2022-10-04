package com.edu.ulab.app.repository;

import com.edu.ulab.app.config.SystemJpaTest;
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
 * Тесты репозитория {@link UserRepository}.
 */
@SystemJpaTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        SQLStatementCountValidator.reset();
    }

    @DisplayName("Сохранить юзера. Число insert должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void insertPerson_thenAssertDmlCount() {
        //Given
        Person person = new Person();
        person.setAge(111);
        person.setTitle("writer");
        person.setFullName("Test Test");

        //When
        Person result = userRepository.save(person);

        //Then
        assertThat(userRepository.count()).isEqualTo(2);
        assertThat(result.getAge()).isEqualTo(111);
        assertSelectCount(1);
        assertInsertCount(1);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    // update

    @DisplayName("Обновить юзера. Число update должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void updatePerson_thenAssertDmlCount() {
        //Given
        Person person = new Person();
        person.setAge(111);
        person.setTitle("writer");
        person.setFullName("new Test");

        //When
        Person existPerson = userRepository.findById(1001L).get();
        existPerson.setFullName(person.getFullName());
        existPerson.setTitle(person.getTitle());
        existPerson.setAge(person.getAge());
        Person result = userRepository.save(existPerson);

        //Then
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(result.getAge()).isEqualTo(111);
        assertThat(result.getFullName()).isEqualTo("new Test");
        assertThat(result.getTitle()).isEqualTo("writer");
        assertSelectCount(2);
        assertInsertCount(0);
        assertUpdateCount(1);
        assertDeleteCount(0);
    }

    // get

    @DisplayName("Получить юзера. Число select должно равняться 2")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void getPerson_thenAssertDmlCount() {
        //Given
        Person person = new Person();
        person.setAge(55);
        person.setTitle("reader");
        person.setFullName("default user");

        //When
        Person result = userRepository.findById(1001L).get();

        //Then
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(result.getAge()).isEqualTo(55);
        assertThat(result.getFullName()).isEqualTo("default user");
        assertThat(result.getTitle()).isEqualTo("reader");
        assertSelectCount(2);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    // get all

    @DisplayName("Получить всех юзеров. Число select должно равняться 3")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void getAllPerson_thenAssertDmlCount() {
        //Given
        Person person = new Person();
        person.setAge(55);
        person.setTitle("writer");
        person.setFullName("default user");

        userRepository.save(person);

        //When
        List<Person> users = (List<Person>) userRepository.findAll();

        //Then
        assertThat(userRepository.count()).isEqualTo(2);
        assertThat(users.size()).isEqualTo(2);
        assertSelectCount(3);
        assertInsertCount(1);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    // delete

    @DisplayName("Удалить юзера. Число delete должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void deletePerson_thenAssertDmlCount() {
        //Given
        Person person = new Person();
        person.setAge(222);
        person.setTitle("writer");
        person.setFullName("new user");

        Person savedPerson = userRepository.save(person);

        //When
        userRepository.delete(savedPerson);

        //Then
        assertThat(userRepository.count()).isEqualTo(1);
        assertSelectCount(1);
        assertInsertCount(1);
        assertUpdateCount(0);
        assertDeleteCount(1);
    }

    // * failed
    @DisplayName("Ошибка при создании юзера.")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void failSavePerson_thenAssertDmlCount() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> userRepository.save(null), "IO error");
    }

    @DisplayName("Ошибка при удалении юзера.")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void failDeletePerson_thenAssertDmlCount() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> userRepository.delete(null), "IO error");
    }

    @DisplayName("Ошибка при получении юзера.")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void failUpdatePerson_thenAssertDmlCount() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> userRepository.findById(null), "IO error");
    }
}
