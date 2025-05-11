package com.library.library_management.service;

import com.library.library_management.dto.requests.BookRequest;
import com.library.library_management.dto.responses.BookResponse;
import com.library.library_management.entity.Book;
import com.library.library_management.repository.BookRepository;
import com.library.library_management.service.contract.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BookServiceImplIntegrationTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @Test
    void testAddBook_Success() {
        BookRequest request = new BookRequest();
        request.setTitle("Test Book");
        request.setAuthor("Test Author");
        request.setIsbn("1234567890");
        request.setGenre("Fiction");

        BookResponse response = bookService.addBook(request);

        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals("Test Book", response.getTitle());
        assertTrue(bookRepository.existsById(response.getId()));
    }

    @Test
    void testGetBookById_Success() {
        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setIsbn("1234567890");
        book.setGenre("Fiction");
        book = bookRepository.save(book);

        BookResponse response = bookService.getBookById(book.getId());

        assertNotNull(response);
        assertEquals(book.getId(), response.getId());
        assertEquals("Test Book", response.getTitle());
    }

    @Test
    void testDeleteBook_Success() {
        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setIsbn("1234567890");
        book.setGenre("Fiction");
        book = bookRepository.save(book);

        bookService.deleteBook(book.getId());

        assertFalse(bookRepository.existsById(book.getId()));
    }
}