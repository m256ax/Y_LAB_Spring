package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.User;
import com.edu.ulab.app.generator.IdGenerator;
import com.edu.ulab.app.generator.impl.UserIdGeneratorImpl;
import com.edu.ulab.app.service.UserService;
import com.edu.ulab.app.storage.UserStorage;
import com.edu.ulab.app.storage.impl.UserStorageImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final IdGenerator idGenerator = new UserIdGeneratorImpl();

    private final UserStorage userStorage = new UserStorageImpl();
    @Override
    public UserDto createUser(UserDto userDto) {
        // сгенерировать идентификатор
        // создать пользователя
        // вернуть сохраненного пользователя со всеми необходимыми полями id
        userDto.setId(idGenerator.next());

        User newUser = User.builder()
                .id(userDto.getId())
                .fullName(userDto.getFullName())
                .title(userDto.getTitle())
                .age(userDto.getAge())
                .build();

        userStorage.save(newUser);

        return userDto;
    }

    @Override
    public UserDto updateUser(UserDto userDto) {


        return null;
    }

    @Override
    public UserDto getUserById(Long id) {

        Optional<User> optionalUser = userStorage.findById(id);

        UserDto existUserDto = new UserDto();

        if(optionalUser.isPresent()){
            User user = optionalUser.get();

            existUserDto.setId(user.getId());
            existUserDto.setFullName(user.getFullName());
            existUserDto.setTitle(user.getTitle());
            existUserDto.setAge(user.getAge());
        }

        return existUserDto;
    }

    @Override
    public void deleteUserById(Long id) {
        userStorage.delete(id);
    }
}
