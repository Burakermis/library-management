package com.library.library_management.mapper;

import com.library.library_management.dto.responses.BorrowingResponse;
import com.library.library_management.entity.Borrowing;
import org.springframework.stereotype.Component;

@Component
public class BorrowingMapper {

    public BorrowingResponse toDto(Borrowing borrowing) {
        BorrowingResponse dto = new BorrowingResponse();
        dto.setId(borrowing.getId());
        dto.setUserId(borrowing.getUser().getId());
        dto.setBookId(borrowing.getBook().getId());
        dto.setBorrowDate(borrowing.getBorrowDate());
        dto.setDueDate(borrowing.getDueDate());
        dto.setReturnDate(borrowing.getReturnDate());
        dto.setReturned(borrowing.isReturned());
        return dto;
    }
}

