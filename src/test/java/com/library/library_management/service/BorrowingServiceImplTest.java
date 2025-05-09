package com.library.library_management.service;

import com.library.library_management.dto.responses.BorrowingResponse;
import com.library.library_management.entity.Book;
import com.library.library_management.entity.Borrowing;
import com.library.library_management.entity.User;
import com.library.library_management.exception.BookNotAvailableException;
import com.library.library_management.exception.BookNotFoundException;
import com.library.library_management.exception.BorrowingNotFoundException;
import com.library.library_management.exception.UserNotFoundException;
import com.library.library_management.mapper.BorrowingMapper;
import com.library.library_management.repository.BookRepository;
import com.library.library_management.repository.BorrowingRepository;
import com.library.library_management.repository.UserRepository;
import com.library.library_management.service.impl.BorrowingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BorrowingServiceImplTest {

    @Mock
    private BorrowingRepository borrowingRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BorrowingMapper borrowingMapper;

    @InjectMocks
    private BorrowingServiceImpl borrowingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBorrowBook_Success() {
        Long userId = 1L;
        Long bookId = 1L;

        User user = new User();
        user.setId(userId);

        Book book = new Book();
        book.setId(bookId);
        book.setAvailable(true);

        Borrowing borrowing = new Borrowing();
        borrowing.setId(1L);
        borrowing.setUser(user);
        borrowing.setBook(book);
        borrowing.setBorrowDate(LocalDate.now());
        borrowing.setDueDate(LocalDate.now().plusWeeks(2));

        BorrowingResponse response = new BorrowingResponse();
        response.setId(1L);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(borrowingRepository.save(any(Borrowing.class))).thenReturn(borrowing);
        when(borrowingMapper.toDto(any(Borrowing.class))).thenReturn(response);

        BorrowingResponse result = borrowingService.borrowBook(userId, bookId);

        assertNotNull(result, "Result should not be null");
        assertEquals(1L, result.getId(), "Borrowing ID should match");
        verify(bookRepository, times(1)).save(book);
        verify(borrowingRepository, times(1)).save(any(Borrowing.class));
    }


    @Test
    void testBorrowBook_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> borrowingService.borrowBook(1L, 1L));
    }

    @Test
    void testBorrowBook_BookNotFound() {
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> borrowingService.borrowBook(1L, 1L));
    }

    @Test
    void testBorrowBook_BookNotAvailable() {
        User user = new User();
        user.setId(1L);

        Book book = new Book();
        book.setId(1L);
        book.setAvailable(false);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        assertThrows(BookNotAvailableException.class, () -> borrowingService.borrowBook(1L, 1L));
    }

    @Test
    void testReturnBook_Success() {
        Long borrowingId = 1L;

        Borrowing borrowing = new Borrowing();
        borrowing.setId(borrowingId);
        borrowing.setReturnDate(null);

        Book book = new Book();
        book.setId(1L);
        borrowing.setBook(book);

        BorrowingResponse response = new BorrowingResponse();
        response.setId(borrowingId);

        when(borrowingRepository.findById(borrowingId)).thenReturn(Optional.of(borrowing));
        when(borrowingMapper.toDto(borrowing)).thenReturn(response);

        BorrowingResponse result = borrowingService.returnBook(borrowingId);

        assertNotNull(result);
        assertEquals(borrowingId, result.getId());
        verify(bookRepository, times(1)).save(book);
        verify(borrowingRepository, times(1)).save(borrowing);
    }

    @Test
    void testReturnBook_BorrowingNotFound() {
        when(borrowingRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BorrowingNotFoundException.class, () -> borrowingService.returnBook(1L));
    }

    @Test
    void testGetUserHistory_Success() {
        Long userId = 1L;

        Borrowing borrowing = new Borrowing();
        borrowing.setId(1L);

        BorrowingResponse response = new BorrowingResponse();
        response.setId(1L);

        when(borrowingRepository.findByUser_Id(userId)).thenReturn(List.of(borrowing));
        when(borrowingMapper.toDto(borrowing)).thenReturn(response);

        List<BorrowingResponse> result = borrowingService.getUserHistory(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void testGetUserHistory_NoBorrowings() {
        when(borrowingRepository.findByUser_Id(1L)).thenReturn(Collections.emptyList());

        assertThrows(BorrowingNotFoundException.class, () -> borrowingService.getUserHistory(1L));
    }

    @Test
    void testGetOverdueBooks_Success() {
        Borrowing borrowing = new Borrowing();
        borrowing.setId(1L);

        BorrowingResponse response = new BorrowingResponse();
        response.setId(1L);

        when(borrowingRepository.findByDueDateBeforeAndReturnDateIsNull(any(LocalDate.class)))
                .thenReturn(List.of(borrowing));
        when(borrowingMapper.toDto(borrowing)).thenReturn(response);

        List<BorrowingResponse> result = borrowingService.getOverdueBooks();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }
}