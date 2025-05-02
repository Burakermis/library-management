package com.library.library_management.dto.responses;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
public class BookResponse {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private LocalDate publicationDate;
    private String genre;
    private boolean available;
}
