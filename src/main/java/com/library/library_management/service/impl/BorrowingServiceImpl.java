package com.library.library_management.service.impl;

import com.library.library_management.exception.BookNotFoundException;
import com.library.library_management.exception.UserNotFoundException;
import com.library.library_management.exception.BookNotAvailableException;
import com.library.library_management.exception.BorrowingNotFoundException;
import com.library.library_management.entity.Book;
import com.library.library_management.entity.Borrowing;
import com.library.library_management.entity.User;
import com.library.library_management.service.contract.BorrowingService;
import com.library.library_management.mapper.BorrowingMapper;
import com.library.library_management.repository.BorrowingRepository;
import com.library.library_management.repository.BookRepository;
import com.library.library_management.repository.UserRepository;
import com.library.library_management.dto.responses.BorrowingResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BorrowingServiceImpl implements BorrowingService {

    private final BorrowingRepository borrowingRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BorrowingMapper borrowingMapper;

    @Override
    public BorrowingResponse borrowBook(Long userId, Long bookId) {
        log.info("Attempting to borrow book with ID {} by user with ID {}", bookId, userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User with ID {} not found", userId);
                    return new UserNotFoundException("User not found");
                });

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> {
                    log.warn("Book with ID {} not found", bookId);
                    return new BookNotFoundException("Book not found");
                });

        if (!book.isAvailable()) {
            log.warn("Book with ID {} is not available", bookId);
            throw new BookNotAvailableException("Book is not available for borrowing.");
        }

        Borrowing borrowing = new Borrowing();
        borrowing.setUser(user);
        borrowing.setBook(book);
        borrowing.setBorrowDate(LocalDate.now());
        borrowing.setDueDate(LocalDate.now().plusWeeks(2));

        book.setAvailable(false);
        bookRepository.save(book);
        borrowingRepository.save(borrowing);

        log.info("Book with ID {} successfully borrowed by user with ID {}", bookId, userId);
        return borrowingMapper.toDto(borrowing);
    }

    @Override
    public BorrowingResponse returnBook(Long borrowingId) {
        log.info("Attempting to return book for borrowing ID {}", borrowingId);

        Borrowing borrowing = borrowingRepository.findById(borrowingId)
                .orElseThrow(() -> {
                    log.warn("Borrowing record with ID {} not found", borrowingId);
                    return new BorrowingNotFoundException("Borrowing record not found");
                });

        borrowing.setReturnDate(LocalDate.now());

        Book book = borrowing.getBook();
        book.setAvailable(true);
        bookRepository.save(book);
        borrowingRepository.save(borrowing);

        log.info("Book with ID {} successfully returned for borrowing ID {}", book.getId(), borrowingId);
        return borrowingMapper.toDto(borrowing);
    }

    @Override
    public List<BorrowingResponse> getUserHistory(Long userId) {
        log.info("Fetching borrowing history for user ID {}", userId);

        List<Borrowing> borrowings = borrowingRepository.findByUserId(userId);
        if (borrowings.isEmpty()) {
            log.warn("No borrowings found for user ID {}", userId);
            throw new BorrowingNotFoundException("No borrowings found for this user.");
        }

        log.debug("Found {} borrowings for user ID {}", borrowings.size(), userId);
        return borrowings.stream().map(borrowingMapper::toDto).toList();
    }

    @Override
    public List<BorrowingResponse> getOverdueBooks() {
        log.info("Fetching overdue borrowings");

        List<Borrowing> borrowings = borrowingRepository.findByDueDateBeforeAndReturnDateIsNull(LocalDate.now());
        log.debug("Found {} overdue borrowings", borrowings.size());

        return borrowings.stream().map(borrowingMapper::toDto).toList();
    }
}

