package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Service
public class BookServiceImplTemplate implements BookService {

    final String INSERT_SQL = "INSERT INTO BOOK (TITLE, AUTHOR, PAGE_COUNT, PERSON_ID) VALUES (?,?,?,?)";

    final String UPDATE_SQL = "UPDATE BOOK SET PERSON_ID = ?, TITLE = ?, AUTHOR = ?, PAGE_COUNT = ? WHERE ID = ?";

    final String SELECT_SQL = "SELECT * FROM BOOK WHERE ID = ?";

    final String DELETE_SQL = "DELETE FROM BOOK WHERE ID = ?";

    final String SELECT_SQL_BOOK_BY_USER_ID = "SELECT id FROM BOOK WHERE PERSON_ID = ?";

    private final JdbcTemplate jdbcTemplate;

    public BookServiceImplTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<BookDto> bookDtoMapper = (row, rowNumber) -> {
        Integer page = row.getObject("PAGE_COUNT", Integer.class);
        return BookDto.builder()
                .id(row.getLong("id"))
                .userId(row.getLong("PERSON_ID"))
                .title(row.getString("TITLE"))
                .author(row.getString("AUTHOR"))
                .pageCount(page)
                .build();
    };

    @Override
    public BookDto createBook(BookDto bookDto) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps =
                                connection.prepareStatement(INSERT_SQL, new String[]{"id"});
                        ps.setString(1, bookDto.getTitle());
                        ps.setString(2, bookDto.getAuthor());
                        ps.setLong(3, bookDto.getPageCount());
                        ps.setLong(4, bookDto.getUserId());
                        log.info("Mapped prepareStatement: {}", ps);
                        return ps;
                    }
                },
                keyHolder);

        bookDto.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        log.info("Book saved: {}", bookDto);
        return bookDto;
    }

    @Override
    public BookDto updateBook(BookDto bookDto) {
        // реализовать недостающие методы
        log.info("Got bookDto: {} for update", bookDto);
        try {
            jdbcTemplate.update(UPDATE_SQL,
                    bookDto.getUserId(),
                    bookDto.getTitle(),
                    bookDto.getAuthor(),
                    bookDto.getPageCount(),
                    bookDto.getId());
        } catch (EmptyResultDataAccessException e) {
            log.info("This bookId is absent in storage");
            throw new NotFoundException("Book is absent");
        }
        log.info("Save updated book to storage: {}", bookDto);
        return bookDto;
    }

    @Override
    public BookDto getBookById(Long id) {
        // реализовать недостающие методы
        log.info("Got id for search: {}", id);
        try {
            return jdbcTemplate.queryForObject(SELECT_SQL, bookDtoMapper, id);
        } catch (EmptyResultDataAccessException e) {
            log.info("This bookId is absent in storage");
            throw new NotFoundException("Book is absent");
        }
    }

    @Override
    public void deleteBookById(Long id) {
        // реализовать недостающие методы
        log.info("Got bookId for delete: {}", id);
        try {
            jdbcTemplate.update(DELETE_SQL, id);
            log.info("Book with id: {} deleted", id);
        } catch (EmptyResultDataAccessException e) {
            log.info("This bookId is absent in storage");
            throw new NotFoundException("Book is absent");
        }
    }

    @Override
    public List<Long> getBookByUserId(Long userId) {
        log.info("Got userId for search book: {}", userId);
        try {
            return jdbcTemplate.query(SELECT_SQL_BOOK_BY_USER_ID,
                    (row, rowNumber) -> row.getLong("id"), userId);
        } catch (EmptyResultDataAccessException e) {
            log.info("Book with userId: {} is absent in storage", userId);
            throw new NotFoundException("Book is absent");
        }
    }
}
