package com.library.library_management.controller;

import com.library.library_management.dto.responses.BorrowingResponse;
import com.library.library_management.service.contract.BorrowingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v0/borrowings")
@RequiredArgsConstructor
public class BorrowingController {

    private final BorrowingService borrowingService;

    @PostMapping("/borrow")
    @PreAuthorize("hasRole('READER')")
    public ResponseEntity<BorrowingResponse> borrowBook(@RequestParam Long userId, @RequestParam Long bookId) {
        BorrowingResponse borrowingResponse = borrowingService.borrowBook(userId, bookId);
        return ResponseEntity.ok(borrowingResponse);
    }

    @PutMapping("/return/{borrowingId}")
    @PreAuthorize("hasRole('READER')")
    public ResponseEntity<BorrowingResponse> returnBook(@PathVariable Long borrowingId) {
        BorrowingResponse returned = borrowingService.returnBook(borrowingId);
        return ResponseEntity.ok(returned);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('READER', 'LIBRARIAN')")
    public ResponseEntity<List<BorrowingResponse>> getUserBorrowingHistory(@PathVariable Long userId) {
        List<BorrowingResponse> history = borrowingService.getUserHistory(userId);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/overdue")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<List<BorrowingResponse>> getOverdueBooks() {
        List<BorrowingResponse> overdue = borrowingService.getOverdueBooks();
        return ResponseEntity.ok(overdue);
    }
}



