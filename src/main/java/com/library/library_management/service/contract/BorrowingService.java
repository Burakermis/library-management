package com.library.library_management.service.contract;

import com.library.library_management.dto.responses.BorrowingResponse;

import java.util.List;

public interface BorrowingService {
    BorrowingResponse borrowBook(Long userId, Long bookId);
    BorrowingResponse returnBook(Long borrowingId);
    List<BorrowingResponse> getUserHistory(Long userId);
    List<BorrowingResponse> getOverdueBooks();
}
