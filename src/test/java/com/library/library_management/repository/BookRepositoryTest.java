package com.library.library_management.repository;

import com.library.library_management.entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        Book book1 = new Book();
        book1.setTitle("Spring Boot in Action");
        book1.setAuthor("Craig Walls");
        book1.setIsbn("1234567890");
        book1.setGenre("Technology");

        Book book2 = new Book();
        book2.setTitle("Clean Code");
        book2.setAuthor("Robert C. Martin");
        book2.setIsbn("0987654321");
        book2.setGenre("Technology");

        bookRepository.saveAll(List.of(book1, book2));
    }

    @Test
    void testSearchBooks_ByTitle() {
        Page<Book> result = bookRepository.searchBooks("Spring Boot", null, null, null, PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Spring Boot in Action", result.getContent().get(0).getTitle());
    }

    @Test
    void testSearchBooks_ByAuthor() {
        Page<Book> result = bookRepository.searchBooks(null, "Robert C. Martin", null, null, PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Clean Code", result.getContent().get(0).getTitle());
    }

    @Test
    void testSearchBooks_ByIsbn() {
        Page<Book> result = bookRepository.searchBooks(null, null, "1234567890", null, PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Spring Boot in Action", result.getContent().get(0).getTitle());
    }

    @Test
    void testSearchBooks_ByGenre() {
        Page<Book> result = bookRepository.searchBooks(null, null, null, "Technology", PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
    }

    @Test
    void testExistsByIsbn() {
        boolean exists = bookRepository.existsByIsbn("1234567890");

        assertTrue(exists);
    }
}