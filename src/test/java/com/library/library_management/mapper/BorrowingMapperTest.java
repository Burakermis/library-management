package com.library.library_management.mapper;

import com.library.library_management.dto.responses.BorrowingResponse;
import com.library.library_management.entity.Book;
import com.library.library_management.entity.Borrowing;
import com.library.library_management.entity.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BorrowingMapperTest {

    private final BorrowingMapper borrowingMapper = new BorrowingMapper();

    @Test
    void testToDto() {
        User user = new User();
        user.setId(1L);

        Book book = new Book();
        book.setId(2L);

        Borrowing borrowing = new Borrowing();
        borrowing.setId(3L);
        borrowing.setUser(user);
        borrowing.setBook(book);
        borrowing.setBorrowDate(LocalDate.of(2023, 1, 1));
        borrowing.setDueDate(LocalDate.of(2023, 1, 15));
        borrowing.setReturnDate(LocalDate.of(2023, 1, 10));

        BorrowingResponse response = borrowingMapper.toDto(borrowing);

        assertNotNull(response);
        assertEquals(3L, response.getId());
        assertEquals(1L, response.getUserId());
        assertEquals(2L, response.getBookId());
        assertEquals(LocalDate.of(2023, 1, 1), response.getBorrowDate());
        assertEquals(LocalDate.of(2023, 1, 15), response.getDueDate());
        assertEquals(LocalDate.of(2023, 1, 10), response.getReturnDate());
    }
}