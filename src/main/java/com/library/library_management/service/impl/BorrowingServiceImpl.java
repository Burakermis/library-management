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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BorrowingServiceImpl implements BorrowingService {

    private final BorrowingRepository borrowingRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BorrowingMapper borrowingMapper;

    @Override
    public BorrowingResponse borrowBook(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found"));

        if (!book.isAvailable()) {
            throw new BookNotAvailableException("Book is not available for borrowing.");
        }

        Borrowing borrowing = new Borrowing();
        borrowing.setUser(user);
        borrowing.setBook(book);
        borrowing.setBorrowDate(LocalDate.now());
        borrowing.setDueDate(LocalDate.now().plusWeeks(2)); // örnek: 2 hafta

        book.setAvailable(false);
        bookRepository.save(book);

        borrowingRepository.save(borrowing);

        return borrowingMapper.toDto(borrowing);
    }

    @Override
    public BorrowingResponse returnBook(Long borrowingId) {
        Borrowing borrowing = borrowingRepository.findById(borrowingId)
                .orElseThrow(() -> new BorrowingNotFoundException("Borrowing record not found"));

        borrowing.setReturnDate(LocalDate.now());

        Book book = borrowing.getBook();
        book.setAvailable(true);
        bookRepository.save(book);

        borrowingRepository.save(borrowing);

        return borrowingMapper.toDto(borrowing);
    }

    @Override
    public List<BorrowingResponse> getUserHistory(Long userId) {
        List<Borrowing> borrowings = borrowingRepository.findByUserId(userId);
        if (borrowings.isEmpty()) {
            throw new BorrowingNotFoundException("No borrowings found for this user.");
        }
        return borrowings.stream().map(borrowingMapper::toDto).toList();
    }

    @Override
    public List<BorrowingResponse> getOverdueBooks() {
        List<Borrowing> borrowings = borrowingRepository.findByDueDateBeforeAndReturnDateIsNull(LocalDate.now());
        return borrowings.stream().map(borrowingMapper::toDto).toList();
    }
}
