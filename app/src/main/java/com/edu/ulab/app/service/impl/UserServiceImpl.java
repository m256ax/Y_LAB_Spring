package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.User;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.generator.IdGenerator;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.service.UserService;
import com.edu.ulab.app.storage.UserStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    @Qualifier("UserId")
    private IdGenerator idGenerator;

    @Autowired
    private UserStorage userStorage;

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        // сгенерировать идентификатор
        // создать пользователя
        // вернуть сохраненного пользователя со всеми необходимыми полями id
        log.info("Got userDto for create: {}", userDto);

        User newUser = userMapper.userDtoToUser(userDto);
        log.info("User mapped from userDto without id{}", newUser);

        if (userStorage.containUser(newUser)) {
            log.info("Find same user {}", newUser);
            throw new IllegalArgumentException("Couldn't create user. This user is present");
        }

        userDto.setId(idGenerator.next());
        log.info("Generated id for user {}", userDto.getId());
        newUser.setId(userDto.getId());
        log.info("User set id: {}", newUser);
        userStorage.save(newUser);
        log.info("User saved to storage: {}", newUser);
        return userDto;
    }

    @Override
    public UserDto updateUser(UserDto userDto) {

        log.info("Got userDto for update: {}", userDto);

        Optional<User> optionalUser = userStorage.findById(userDto.getId());

        if (optionalUser.isEmpty()) {
            throw new NotFoundException("User is absent");
        }

        User existUser = optionalUser.get();
        log.info("User is present in storage {}", existUser);
        existUser.setFullName(userDto.getFullName());
        existUser.setTitle(userDto.getTitle());
        existUser.setAge(userDto.getAge());
        log.info("Updated user : {}", existUser);
        userStorage.update(existUser);
        log.info("Updated user saved to storage");

        return userMapper.userToUserDto(existUser);
    }

    @Override
    public UserDto getUserById(Long userId) {
        log.info("Got userId for search: {}", userId);

        Optional<User> optionalUser = userStorage.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new NotFoundException("User is absent");
        }

        UserDto existUserDto = new UserDto();
        log.info("Create empty user: {}", existUserDto);

        User existUser = optionalUser.get();
        log.info("User is present in storage {}", existUser);
        existUserDto = userMapper.userToUserDto(existUser);
        log.info("Existed user mapped to userDto : {}", existUserDto);

        return existUserDto;
    }

    @Override
    public void deleteUserById(Long userId) {

        log.info("Got userId for delete: {}", userId);

        Optional<User> optionalUser = userStorage.findById(userId);

        if (optionalUser.isEmpty()) {
            log.info("This userId is absent in storage");
            throw new NotFoundException("This user id absent");
        }

        userStorage.delete(userId);
        log.info("User with userId: {} is deleted from storage", userId);
    }
}
