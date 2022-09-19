package com.edu.ulab.app.mapper;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.Book.BookBuilder;
import com.edu.ulab.app.web.request.BookRequest;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-09-19T18:46:55+0700",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.4.1 (Oracle Corporation)"
)
@Component
public class BookMapperImpl implements BookMapper {

    @Override
    public BookDto bookRequestToBookDto(BookRequest bookRequest) {
        if ( bookRequest == null ) {
            return null;
        }

        BookDto bookDto = new BookDto();

        bookDto.setTitle( bookRequest.getTitle() );
        bookDto.setAuthor( bookRequest.getAuthor() );
        bookDto.setPageCount( bookRequest.getPageCount() );

        return bookDto;
    }

    @Override
    public Book BookDtoToBook(BookDto bookDto) {
        if ( bookDto == null ) {
            return null;
        }

        BookBuilder book = Book.builder();

        book.id( bookDto.getId() );
        book.userId( bookDto.getUserId() );
        book.title( bookDto.getTitle() );
        book.author( bookDto.getAuthor() );
        book.pageCount( bookDto.getPageCount() );

        return book.build();
    }

    @Override
    public BookDto BookToBookDto(Book book) {
        if ( book == null ) {
            return null;
        }

        BookDto bookDto = new BookDto();

        bookDto.setId( book.getId() );
        bookDto.setUserId( book.getUserId() );
        bookDto.setTitle( book.getTitle() );
        bookDto.setAuthor( book.getAuthor() );
        bookDto.setPageCount( book.getPageCount() );

        return bookDto;
    }
}
