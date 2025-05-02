package com.library.library_management.mapper;

import com.library.library_management.dto.requests.BookRequest;
import com.library.library_management.dto.responses.BookResponse;
import com.library.library_management.entity.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public Book toEntity(BookRequest dto) {
        return Book.builder()
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .isbn(dto.getIsbn())
                .publicationDate(dto.getPublicationDate())
                .genre(dto.getGenre())
                .available(true)
                .build();
    }

    public BookResponse toDto(Book entity) {
        return BookResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .author(entity.getAuthor())
                .isbn(entity.getIsbn())
                .publicationDate(entity.getPublicationDate())
                .genre(entity.getGenre())
                .available(entity.isAvailable())
                .build();
    }
}

