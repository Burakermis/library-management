package com.library.library_management.repository;

import com.library.library_management.entity.Borrowing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BorrowingRepository extends JpaRepository<Borrowing, Long> {
    List<Borrowing> findByUserId(Long userId);
    List<Borrowing> findByDueDateBeforeAndReturnDateIsNull(LocalDate date);

}

