package com.edu.ulab.app.mapper;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.web.request.BookRequest;
import com.edu.ulab.app.web.response.BookResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookDto bookRequestToBookDto(BookRequest bookRequest);

    BookRequest bookDtoToBookRequest(BookDto bookDto);

    Book bookDtoToBook(BookDto bookDto);

    BookDto bookToBookDto(Book book);

    @Mapping(target="id", source="id")
    BookResponse bookDtoToBookResponse(BookDto bookDto);

    default List<Long> createListId(Optional<List<Book>> optional) {
        return optional.get().stream().map(Book::getId).collect(Collectors.toList());
    }
}
