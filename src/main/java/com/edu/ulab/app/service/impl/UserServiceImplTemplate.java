package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;

import java.util.Objects;

@Slf4j
@Service
public class UserServiceImplTemplate implements UserService {

    private final String INSERT_SQL = "INSERT INTO PERSON(FULL_NAME, TITLE, AGE) VALUES (?,?,?)";

    final String UPDATE_SQL = "UPDATE PERSON SET FULL_NAME = ?, TITLE = ?, AGE = ? WHERE id = ?";

    final String SELECT_SQL = "SELECT * FROM PERSON WHERE id = ?";

    final String DELETE_SQL = "DELETE FROM PERSON WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;

    public UserServiceImplTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<UserDto> userDtoMapper = (row, rowNumber) -> {
        Integer age = row.getObject("age", Integer.class);
        return UserDto.builder()
                .id(row.getLong("id"))
                .fullName(row.getString("FULL_NAME"))
                .title(row.getString("TITLE"))
                .age(age)
                .build();
    };

    @Override
    public UserDto createUser(UserDto userDto) {

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(INSERT_SQL, new String[]{"id"});
                    ps.setString(1, userDto.getFullName());
                    ps.setString(2, userDto.getTitle());
                    ps.setLong(3, userDto.getAge());
                    log.info("Mapped user: {}", ps);
                    return ps;
                }, keyHolder);

        userDto.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        log.info("Saved user: {}", userDto);
        return userDto;
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        // реализовать недостающие методы
        log.info("Got userDto for update: {}", userDto);
        try {
            jdbcTemplate.update(UPDATE_SQL,
                    userDto.getFullName(),
                    userDto.getTitle(),
                    userDto.getAge(),
                    userDto.getId());
            log.info("Updated user saved to storage");
        } catch (EmptyResultDataAccessException e) {
            log.info("User is absent");
            throw new NotFoundException("User is absent");
        }
        return userDto;
    }

    @Override
    public UserDto getUserById(Long id) {
        // реализовать недостающие методы
        log.info("Got userId for search: {}", id);
        try {
            return jdbcTemplate.queryForObject(SELECT_SQL, userDtoMapper, id);
        } catch (EmptyResultDataAccessException e) {
            log.info("User is absent");
            throw new NotFoundException("User is absent");
        }
    }

    @Override
    public void deleteUserById(Long id) {
        // реализовать недостающие методы
        log.info("Got userId for delete: {}", id);
        try {
            jdbcTemplate.update(DELETE_SQL, id);
        } catch (EmptyResultDataAccessException e) {
            log.info("User is absent");
            throw new NotFoundException("User is absent");
        }
        log.info("User with id: {} deleted", id);
    }
}
