package com.library.library_management.mapper;

import com.library.library_management.dto.requests.BookRequest;
import com.library.library_management.dto.responses.BookResponse;
import com.library.library_management.entity.Book;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BookMapperTest {

    private final BookMapper bookMapper = new BookMapper();

    @Test
    void testToEntity() {
        BookRequest request = BookRequest.builder()
                .title("Test Title")
                .author("Test Author")
                .isbn("123456789")
                .publicationDate(LocalDate.of(2023, 1, 1))
                .genre("Fiction")
                .build();

        Book book = bookMapper.toEntity(request);

        assertNotNull(book);
        assertEquals("Test Title", book.getTitle());
        assertEquals("Test Author", book.getAuthor());
        assertEquals("123456789", book.getIsbn());
        assertEquals(LocalDate.of(2023, 1, 1), book.getPublicationDate());
        assertEquals("Fiction", book.getGenre());
        assertTrue(book.isAvailable());
    }

    @Test
    void testToDto() {
        Book book = Book.builder()
                .id(1L)
                .title("Test Title")
                .author("Test Author")
                .isbn("123456789")
                .publicationDate(LocalDate.of(2023, 1, 1))
                .genre("Fiction")
                .available(true)
                .build();

        BookResponse response = bookMapper.toDto(book);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Test Title", response.getTitle());
        assertEquals("Test Author", response.getAuthor());
        assertEquals("123456789", response.getIsbn());
        assertEquals(LocalDate.of(2023, 1, 1), response.getPublicationDate());
        assertEquals("Fiction", response.getGenre());
        assertTrue(response.isAvailable());
    }
}