package com.edu.ulab.app.facade;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.service.UserService;
import com.edu.ulab.app.web.request.UserBookRequest;
import com.edu.ulab.app.web.response.UserBookResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class UserDataFacade {
    private final UserService userService;
    private final BookService bookService;
    private final UserMapper userMapper;
    private final BookMapper bookMapper;

    public UserDataFacade(UserService userService,
                          BookService bookService,
                          UserMapper userMapper,
                          BookMapper bookMapper) {
        this.userService = userService;
        this.bookService = bookService;
        this.userMapper = userMapper;
        this.bookMapper = bookMapper;
    }

    private List<Long> saveBook(UserBookRequest userBookRequest, UserDto createdUser) {
        List<Long> bookIdList = userBookRequest.getBookRequests()
                .stream()
                .filter(Objects::nonNull)
                .map(bookMapper::bookRequestToBookDto)
                .peek(bookDto -> bookDto.setUserId(createdUser.getId()))
                .peek(mappedBookDto -> log.info("mapped book: {}", mappedBookDto))
                .map(bookService::createBook)
                .peek(createdBook -> log.info("Created book: {}", createdBook))
                .map(BookDto::getId)
                .toList();
        log.info("Collected book ids: {}", bookIdList);
        return bookIdList;
    }

    public UserBookResponse createUserWithBooks(UserBookRequest userBookRequest) {
        log.info("Got user book create request: {}", userBookRequest);
        UserDto userDto = userMapper.userRequestToUserDto(userBookRequest.getUserRequest());
        log.info("Mapped user request: {}", userDto);

        UserDto createdUser = userService.createUser(userDto);
        log.info("Created user: {}", createdUser);

        List<Long> bookIdList = saveBook(userBookRequest, createdUser);

        return UserBookResponse.builder()
                .userId(createdUser.getId())
                .booksIdList(bookIdList)
                .build();
    }

    public UserBookResponse updateUserWithBooks(Long userId, UserBookRequest userBookRequest) {
        log.info("Got userId: {}", userId);
        log.info("Got userBookRequest{}", userBookRequest);
        UserDto existUser = userService.getUserById(userId);
        log.info("Got user by id: {}", existUser);
        UserDto newUser = userMapper.userRequestToUserDto(userBookRequest.getUserRequest());
        log.info("Got new user: {}", newUser);

        existUser.setFullName(newUser.getFullName());
        existUser.setTitle(newUser.getTitle());
        existUser.setAge(newUser.getAge());
        log.info("Set fields for existUser: {}", existUser);
        userService.updateUser(existUser);
        log.info("Exist user saved to storage: {}", existUser);


        bookService.getByUserId(userId).forEach(bookService::deleteBookById);
        log.info("Delete all book in the storage with userId: {}", userId);

        List<Long> bookIdList = saveBook(userBookRequest, existUser);

        return UserBookResponse.builder()
                .userId(userId)
                .booksIdList(bookIdList)
                .build();
    }

    public UserBookResponse getUserWithBooks(Long userId) {
        log.info("Got user id for prepare userBookResponse : {}", userId);

        userService.getUserById(userId);

        List<Long> bookIdList = bookService.getByUserId(userId);

        if (!bookIdList.isEmpty()) {
            return UserBookResponse.builder()
                    .userId(userId)
                    .booksIdList(bookIdList)
                    .build();
        }

        return UserBookResponse.builder().userId(userId).build();
    }

    public void deleteUserWithBooks(Long userId) {
        userService.getUserById(userId);

        List<Long> bookIdList = bookService.getByUserId(userId);
        log.info("book list for user Id: {}", bookIdList);

        if (!bookIdList.isEmpty()) {
            bookIdList.forEach(bookService::deleteBookById);
        }
        userService.deleteUserById(userId);
    }

}
