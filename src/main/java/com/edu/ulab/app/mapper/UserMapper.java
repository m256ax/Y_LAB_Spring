package com.edu.ulab.app.mapper;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.web.request.UserRequest;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto userRequestToUserDto(UserRequest userRequest);

    UserRequest userDtoToUserRequest(UserDto userDto);

    Person userDtoToPerson(UserDto userDto);

    UserDto personToUserDto(Person person);

    default List<Long> createListId(List<Person> people) {
        return people.stream().map(Person::getId).collect(Collectors.toList());
    }
}
