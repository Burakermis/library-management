package com.library.library_management.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Borrowing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Book book;

    private LocalDate borrowDate;

    private LocalDate dueDate;

    private LocalDate returnDate;

    public boolean isReturned() {
        return returnDate != null;
    }
}

