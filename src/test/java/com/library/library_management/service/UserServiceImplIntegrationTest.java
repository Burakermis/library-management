package com.library.library_management.service;

import com.library.library_management.dto.requests.UserRequest;
import com.library.library_management.dto.responses.UserResponse;
import com.library.library_management.entity.Role;
import com.library.library_management.entity.User;
import com.library.library_management.repository.UserRepository;
import com.library.library_management.service.contract.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceImplIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void testRegisterUser_Success() {
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail("test@example.com");
        userRequest.setPassword("password123");
        userRequest.setName("Test User");

        UserResponse response = userService.registerUser(userRequest);

        assertNotNull(response);
        assertEquals("test@example.com", response.getEmail());
    }

    @Test
    void testLogin_Success() {
        User user = new User();
        user.setEmail("login@example.com");
        user.setRole(Role.READER);
        user.setPassword(passwordEncoder.encode("password123"));
        user.setName("Login User");
        userRepository.save(user);

        String token = userService.login("login@example.com", "password123");

        assertNotNull(token);
    }

    @Test
    void testUpdateUser_Success() {
        User user = new User();
        user.setEmail("update@example.com");
        user.setRole(Role.READER);
        user.setPassword(passwordEncoder.encode("password123"));
        user.setName("Update User");
        userRepository.save(user);

        UserRequest updatedRequest = new UserRequest();
        updatedRequest.setEmail("update@example.com");
        updatedRequest.setPassword("newpassword123");
        updatedRequest.setName("Updated User");

        UserResponse response = userService.updateUser(updatedRequest);

        assertNotNull(response);
        assertEquals("Updated User", response.getName());
    }
}