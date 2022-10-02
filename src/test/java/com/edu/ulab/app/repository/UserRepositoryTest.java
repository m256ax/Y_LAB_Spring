package com.edu.ulab.app.repository;

import com.edu.ulab.app.config.SystemJpaTest;
import com.edu.ulab.app.entity.Person;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static com.vladmihalcea.sql.SQLStatementCountValidator.*;
import static org.assertj.core.api.Assertions.assertThat;

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

    @DisplayName("Сохранить юзера. Число select должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void insertPerson_thenAssertDmlCount() {
        //Given
        reset();
        Person person = new Person();
        person.setAge(111);
        person.setTitle("reader");
        person.setFullName("Test Test");

        //When
        Person result = userRepository.save(person);

        //Then
        assertThat(result.getAge()).isEqualTo(111);
        assertSelectCount(0);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    // update

    @DisplayName("Обновить юзера. Число select должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void updatePerson_thenAssertDmlCount() {
        //Given
        reset();
        Person person = new Person();
        person.setAge(111);
        person.setTitle("reader");
        person.setFullName("Test Test");

        //When
        Person existPerson = userRepository.findById(1001L).get();
        existPerson.setFullName(person.getFullName());
        existPerson.setTitle(person.getTitle());
        existPerson.setAge(person.getAge());
        Person result = userRepository.save(person);

        //Then
        assertThat(result.getAge()).isEqualTo(111);
        assertThat(result.getFullName()).isEqualTo("Test Test");
        assertThat(result.getTitle()).isEqualTo("reader");
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    // get

    @DisplayName("Получить юзера. Число select должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void getPerson_thenAssertDmlCount() {
        //Given
        reset();
        Person person = new Person();
        person.setAge(55);
        person.setTitle("reader");
        person.setFullName("default user");

        //When
        Person result = userRepository.findById(1001L).get();

        //Then
        assertThat(result.getAge()).isEqualTo(55);
        assertThat(result.getFullName()).isEqualTo("default user");
        assertThat(result.getTitle()).isEqualTo("reader");
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    // get all

    @DisplayName("Получить всех юзеров. Число select должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void getAllPerson_thenAssertDmlCount() {
        //Given
        reset();
        Person person = new Person();
        person.setAge(55);
        person.setTitle("writer");
        person.setFullName("default user");

        userRepository.save(person);

        //When
        List<Person> users = (List<Person>) userRepository.findAll();

        //Then
        assertThat(users.size()).isEqualTo(2);
        assertSelectCount(2);
        assertInsertCount(1);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    // delete

    @DisplayName("Удалить юзера. Число select должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void deletePerson_thenAssertDmlCount() {
        //Given
        reset();
        Person person = new Person();
        person.setId(2222L);
        person.setAge(222);
        person.setTitle("writer");
        person.setFullName("new user");

        userRepository.save(person);

        //When
        userRepository.delete(person);

        //Then
        assertSelectCount(2);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    // * failed
}
