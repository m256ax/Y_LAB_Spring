package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.repository.UserRepository;
import com.edu.ulab.app.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository,
                           UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        Person user = userMapper.userDtoToPerson(userDto);
        log.info("Mapped user: {}", user);
        Person savedUser = userRepository.save(user);
        log.info("Saved user: {}", savedUser);
        return userMapper.personToUserDto(savedUser);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {

        log.info("Got userDto for update: {}", userDto);

        Optional<Person> optionalPerson = userRepository.findById(userDto.getId());

        nullChecker(optionalPerson);

        Person existPerson = optionalPerson.get();
        log.info("User is present in storage {}", existPerson);
        existPerson = userMapper.userDtoToPerson(userDto);
        log.info("Updated user : {}", existPerson);
        Person savedPerson = userRepository.save(existPerson);
        log.info("Updated user saved to storage");

        return userMapper.personToUserDto(savedPerson);
    }

    @Override
    public UserDto getUserById(Long userId) {
        log.info("Got userId for search: {}", userId);

        Optional<Person> optionalPerson = userRepository.findById(userId);

        nullChecker(optionalPerson);

        UserDto existUserDto = new UserDto();
        log.info("Create empty user: {}", existUserDto);

        Person existPerson = optionalPerson.get();
        log.info("User is present in storage {}", existPerson);
        existUserDto = userMapper.personToUserDto(existPerson);
        log.info("Existed user mapped to userDto : {}", existUserDto);

        return existUserDto;
    }

    private static void nullChecker(Optional<Person> optionalPerson) {
        if (optionalPerson.isEmpty()) {
            log.info("User is absent");
            throw new NotFoundException("User is absent");
        }
    }

    @Override
    public void deleteUserById(Long userId) {

        log.info("Got userId for delete: {}", userId);

        Optional<Person> optionalPerson = userRepository.findById(userId);

        nullChecker(optionalPerson);

        userRepository.delete(optionalPerson.get());
        log.info("User with userId: {} is deleted from storage", userId);
    }

    @Override
    public List<Long> getAllUser() {
        List<Person> people = (List<Person>) userRepository.findAll();
        return userMapper.createListId(people);
    }
}
