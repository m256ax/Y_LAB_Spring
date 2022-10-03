package com.edu.ulab.app.service;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.repository.UserRepository;
import com.edu.ulab.app.service.impl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Тестирование функционала {@link com.edu.ulab.app.service.impl.UserServiceImpl}.
 */
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DisplayName("Testing user functionality.")
public class UserServiceImplTest {
    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @Test
    @DisplayName("Создание пользователя. Должно пройти успешно.")
    void savePerson_Test() {
        //given

        UserDto userDto = new UserDto();
        userDto.setAge(11);
        userDto.setFullName("test name");
        userDto.setTitle("test title");

        Person person = new Person();
        person.setFullName("test name");
        person.setAge(11);
        person.setTitle("test title");

        Person savedPerson = new Person();
        savedPerson.setId(1L);
        savedPerson.setFullName("test name");
        savedPerson.setAge(11);
        savedPerson.setTitle("test title");

        UserDto result = new UserDto();
        result.setId(1L);
        result.setAge(11);
        result.setFullName("test name");
        result.setTitle("test title");

        //when

        when(userMapper.userDtoToPerson(userDto)).thenReturn(person);
        when(userRepository.save(person)).thenReturn(savedPerson);
        when(userMapper.personToUserDto(savedPerson)).thenReturn(result);

        //then

        UserDto userDtoResult = userService.createUser(userDto);
        assertEquals(1L, userDtoResult.getId());
    }

    // update
    @Test
    @DisplayName("Обновление пользователя. Должно пройти успешно.")
    void updatePerson_Test() {
        //given

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setAge(46);
        userDto.setFullName("new name");
        userDto.setTitle("new title");

        Person updatedPerson = new Person();
        userDto.setId(1L);
        updatedPerson.setFullName("new name");
        updatedPerson.setAge(46);
        updatedPerson.setTitle("new title");

        Person existPerson = new Person();
        existPerson.setId(1L);
        existPerson.setFullName("test name");
        existPerson.setAge(11);
        existPerson.setTitle("test title");

        Person savedPerson = new Person();
        savedPerson.setId(1L);
        savedPerson.setFullName("new name");
        savedPerson.setAge(46);
        savedPerson.setTitle("new title");

        UserDto result = new UserDto();
        result.setId(1L);
        result.setAge(46);
        result.setFullName("new name");
        result.setTitle("new title");

        //when

        when(userRepository.findById(userDto.getId())).thenReturn(Optional.of(existPerson));
        when(userMapper.userDtoToPerson(userDto)).thenReturn(updatedPerson);
        when(userRepository.save(updatedPerson)).thenReturn(savedPerson);
        when(userMapper.personToUserDto(savedPerson)).thenReturn(result);

        //then

        UserDto userDtoResult = userService.createUser(userDto);
        assertEquals(1L, userDtoResult.getId());
    }

    // get
    @Test
    @DisplayName("Получение пользователя. Должно пройти успешно.")
    void getPerson_Test() {
        //given

        UserDto userDto = new UserDto();
        userDto.setId(1L);

        Person existPerson = new Person();
        existPerson.setId(1L);
        existPerson.setFullName("test name");
        existPerson.setTitle("test title");
        existPerson.setAge(11);

        UserDto result = new UserDto();
        result.setId(1L);
        result.setTitle("test title");
        result.setFullName("test name");
        result.setAge(11);

        //when

        when(userRepository.findById(userDto.getId())).thenReturn(Optional.of(existPerson));
        when(userMapper.personToUserDto(existPerson)).thenReturn(result);

        //then

        UserDto userDtoResult = userService.getUserById(1L);
        assertEquals(1L, userDtoResult.getId());
        assertEquals("test name", userDtoResult.getFullName());
        assertEquals("test title", userDtoResult.getTitle());
        assertEquals(11, userDtoResult.getAge());
    }

    // get all
    @Test
    @DisplayName("Получение всех пользователей. Должно пройти успешно.")
    void getAllPerson_Test() {
        //given

        List<Long> testResult = new ArrayList<>();
        testResult.add(1L);

        Person existPerson = new Person();
        existPerson.setId(1L);
        existPerson.setFullName("test name");
        existPerson.setTitle("test title");
        existPerson.setAge(11);

        //when

        when(userRepository.findAll()).thenReturn(Collections.singletonList(existPerson));
        when(userMapper.createListId(Collections.singletonList(existPerson))).thenReturn(List.of(1L));

        //then
        List<Long> result = userService.getAllUser();
        assertEquals(testResult.get(0), result.get(0));
    }


    // delete
    @Test
    @DisplayName("Удаление пользователя. Должно пройти успешно.")
    void deletePerson_Test() {
        //given

        UserDto userDto = new UserDto();
        userDto.setId(1L);

        Person existPerson = new Person();
        existPerson.setId(1L);
        existPerson.setFullName("test name");
        existPerson.setTitle("test title");
        existPerson.setAge(11);

        //when

        when(userRepository.findById(userDto.getId())).thenReturn(Optional.of(existPerson));
        userRepository.delete(existPerson);

        //then

        verify(userRepository).delete(existPerson);
    }

    // * failed
    @Test
    @DisplayName("Ошибка при создание пользователя. Должно пройти успешно.")
    void failToSavePerson_Test() {
        //given

        UserDto userDto = new UserDto();
        userDto.setAge(11);
        userDto.setFullName("test name");
        userDto.setTitle("test title");

        Person person = new Person();
        person.setFullName("test name");
        person.setAge(11);
        person.setTitle("test title");

        //when
        doThrow(NotFoundException.class).when(userRepository).save(same(person));

        //then

        assertThatThrownBy(() -> userService.createUser(userDto))
                .isInstanceOf(SQLIntegrityConstraintViolationException.class);
    }

    //         doThrow(dataInvalidException).when(testRepository)
    //                .save(same(test));
    // example failed
    //  assertThatThrownBy(() -> testeService.createTest(testRequest))
    //                .isInstanceOf(DataInvalidException.class)
    //                .hasMessage("Invalid data set");
}
