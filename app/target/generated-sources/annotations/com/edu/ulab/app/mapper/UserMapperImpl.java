package com.edu.ulab.app.mapper;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.User;
import com.edu.ulab.app.entity.User.UserBuilder;
import com.edu.ulab.app.web.request.UserRequest;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-09-19T18:46:55+0700",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.4.1 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto userRequestToUserDto(UserRequest userRequest) {
        if ( userRequest == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setFullName( userRequest.getFullName() );
        userDto.setTitle( userRequest.getTitle() );
        userDto.setAge( userRequest.getAge() );

        return userDto;
    }

    @Override
    public User userDtoToUser(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        UserBuilder user = User.builder();

        user.id( userDto.getId() );
        user.fullName( userDto.getFullName() );
        user.title( userDto.getTitle() );
        user.age( userDto.getAge() );

        return user.build();
    }

    @Override
    public UserDto userToUserDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setId( user.getId() );
        userDto.setFullName( user.getFullName() );
        userDto.setTitle( user.getTitle() );
        userDto.setAge( user.getAge() );

        return userDto;
    }
}
