package com.library.library_management.repository;

import com.library.library_management.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setName("User One");

        User user2 = new User();
        user2.setEmail("user2@example.com");
        user2.setName("User Two");

        userRepository.save(user1);
        userRepository.save(user2);
    }

    @Test
    void testFindByEmail_UserExists() {
        Optional<User> result = userRepository.findByEmail("user1@example.com");

        assertTrue(result.isPresent());
        assertEquals("User One", result.get().getName());
    }

    @Test
    void testFindByEmail_UserDoesNotExist() {
        Optional<User> result = userRepository.findByEmail("nonexistent@example.com");

        assertFalse(result.isPresent());
    }
}