package com.library.library_management.dto.responses;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BorrowingResponse {
    private Long id;
    private Long userId;
    private Long bookId;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private boolean returned;
}

