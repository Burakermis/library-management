package com.library.library_management.repository;

import com.library.library_management.entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
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

        bookRepository.save(book1);
        bookRepository.save(book2);
    }

    @Test
    void testExistsByIsbn() {
        boolean exists = bookRepository.existsByIsbn("1234567890");
        assertTrue(exists);

        boolean notExists = bookRepository.existsByIsbn("1111111111");
        assertFalse(notExists);
    }

    @Test
    void testFindById() {
        Book savedBook = bookRepository.findAll().getFirst();
        Long bookId = savedBook.getId();

        Optional<Book> book = bookRepository.findById(bookId);
        assertTrue(book.isPresent());
        assertEquals("Spring Boot in Action", book.get().getTitle());
    }
}