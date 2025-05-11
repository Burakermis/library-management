package com.library.library_management.service;

import com.library.library_management.dto.responses.BorrowingResponse;
import com.library.library_management.entity.Book;
import com.library.library_management.entity.Borrowing;
import com.library.library_management.entity.User;
import com.library.library_management.repository.BookRepository;
import com.library.library_management.repository.BorrowingRepository;
import com.library.library_management.repository.UserRepository;
import com.library.library_management.service.contract.BorrowingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BorrowingServiceImplIntegrationTest {

    @Autowired
    private BorrowingService borrowingService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BorrowingRepository borrowingRepository;

    @Test
    void testBorrowBook_Success() {
        User user = new User();
        user.setName("Test User");
        user = userRepository.save(user);

        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setIsbn("1234567890");
        book.setAvailable(true);
        book = bookRepository.save(book);

        BorrowingResponse response = borrowingService.borrowBook(user.getId(), book.getId());

        assertNotNull(response);
        assertNotNull(response.getId());
        assertFalse(bookRepository.findById(book.getId())
                .orElseThrow(() -> new IllegalArgumentException("Kitap bulunamadı"))
                .isAvailable());
        assertTrue(borrowingRepository.existsById(response.getId()));
    }

    @Test
    void testReturnBook_Success() {
        User user = new User();
        user.setName("Test User");
        user = userRepository.save(user);

        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setIsbn("1234567890");
        book.setAvailable(false);
        book = bookRepository.save(book);

        Borrowing borrowing = new Borrowing();
        borrowing.setUser(user);
        borrowing.setBook(book);
        borrowing.setBorrowDate(LocalDate.now().minusDays(10));
        borrowing.setDueDate(LocalDate.now().plusDays(4));
        borrowing = borrowingRepository.save(borrowing);

        BorrowingResponse response = borrowingService.returnBook(borrowing.getId());

        assertNotNull(response);
        assertNotNull(response.getReturnDate());
        assertTrue(bookRepository.findById(book.getId())
                .orElseThrow(() -> new IllegalArgumentException("Kitap bulunamadı"))
                .isAvailable());
    }

    @Test
    void testGetUserHistory_Success() {
        User user = new User();
        user.setName("Test User");
        user = userRepository.save(user);

        Book book = new Book();
        book.setTitle("Book 1");
        book.setAuthor("Author 1");
        book.setIsbn("1111111111");
        book.setAvailable(false);
        book = bookRepository.save(book);

        Borrowing borrowing = new Borrowing();
        borrowing.setUser(user);
        borrowing.setBook(book);
        borrowing.setBorrowDate(LocalDate.now().minusDays(10));
        borrowing.setDueDate(LocalDate.now().plusDays(4));
        borrowingRepository.save(borrowing);

        List<BorrowingResponse> history = borrowingService.getUserHistory(user.getId());

        assertNotNull(history);
        assertEquals(1, history.size());
    }

    @Test
    void testGetOverdueBooks_Success() {
        User user = new User();
        user.setName("Test User");
        user = userRepository.save(user);

        Book book = new Book();
        book.setTitle("Overdue Book");
        book.setAuthor("Author");
        book.setIsbn("2222222222");
        book.setAvailable(false);
        book = bookRepository.save(book);

        Borrowing borrowing = new Borrowing();
        borrowing.setUser(user);
        borrowing.setBook(book);
        borrowing.setBorrowDate(LocalDate.now().minusDays(20));
        borrowing.setDueDate(LocalDate.now().minusDays(5));
        borrowingRepository.save(borrowing);

        List<BorrowingResponse> overdueBooks = borrowingService.getOverdueBooks();

        assertNotNull(overdueBooks);
        assertEquals(1, overdueBooks.size());
    }
}