package com.library.library_management.repository;

import com.library.library_management.entity.Borrowing;
import com.library.library_management.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class BorrowingRepositoryTest {

    @Autowired
    private BorrowingRepository borrowingRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user1 = new User();
        user1.setName("User One");
        user1.setEmail("user1@example.com");
        user1 = userRepository.save(user1);

        User user2 = new User();
        user2.setName("User Two");
        user2.setEmail("user2@example.com");
        user2 = userRepository.save(user2);

        Borrowing borrowing1 = new Borrowing();
        borrowing1.setUser(user1);
        borrowing1.setDueDate(LocalDate.now().minusDays(1));
        borrowing1.setReturnDate(null);

        Borrowing borrowing2 = new Borrowing();
        borrowing2.setUser(user1);
        borrowing2.setDueDate(LocalDate.now().plusDays(5));
        borrowing2.setReturnDate(LocalDate.now());

        Borrowing borrowing3 = new Borrowing();
        borrowing3.setUser(user2);
        borrowing3.setDueDate(LocalDate.now().minusDays(2));
        borrowing3.setReturnDate(null);

        borrowingRepository.saveAll(List.of(borrowing1, borrowing2, borrowing3));
    }

    @Test
    void testFindByUserId() {
        User user1 = userRepository.findByEmail("user1@example.com").orElseThrow();
        List<Borrowing> result = borrowingRepository.findByUser_Id(user1.getId());

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testFindByDueDateBeforeAndReturnDateIsNull() {
        List<Borrowing> result = borrowingRepository.findByDueDateBeforeAndReturnDateIsNull(LocalDate.now());

        assertNotNull(result);
        assertEquals(2, result.size());
    }
}