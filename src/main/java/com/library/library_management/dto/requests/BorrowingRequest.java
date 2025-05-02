package com.library.library_management.dto.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BorrowingRequest {
    @NotNull
    private Long userId;

    @NotNull
    private Long bookId;
}

