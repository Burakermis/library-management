package com.library.library_management.service;

import com.library.library_management.dto.requests.UserRequest;
import com.library.library_management.dto.responses.UserResponse;
import com.library.library_management.entity.Role;
import com.library.library_management.entity.User;
import com.library.library_management.mapper.UserMapper;
import com.library.library_management.repository.UserRepository;
import com.library.library_management.security.JwtTokenProvider;
import com.library.library_management.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_Success() {
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail("test@example.com");
        userRequest.setPassword("password123");

        User user = new User();
        user.setEmail("test@example.com");

        User savedUser = new User();
        savedUser.setEmail("test@example.com");

        UserResponse userResponse = new UserResponse();
        userResponse.setEmail("test@example.com");

        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.empty());
        when(userMapper.toEntity(userRequest)).thenReturn(user);
        when(passwordEncoder.encode(userRequest.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userMapper.toDto(savedUser)).thenReturn(userResponse);

        UserResponse result = userService.registerUser(userRequest);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testLogin_Success() {
        String email = "login@example.com";
        String password = "password123";

        User user = new User();
        user.setEmail(email);
        user.setPassword("encodedPassword");
        user.setRole(Role.READER);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);
        when(jwtTokenProvider.generateToken(email, user.getRole())).thenReturn("mockToken");

        String token = userService.login(email, password);

        assertNotNull(token);
        assertEquals("mockToken", token);
    }

    @Test
    void testUpdateUser_Success() {
        UserRequest updatedRequest = new UserRequest();
        updatedRequest.setEmail("update@example.com");
        updatedRequest.setPassword("newpassword123");

        User user = new User();
        user.setEmail("update@example.com");

        User savedUser = new User();
        savedUser.setEmail("update@example.com");

        UserResponse userResponse = new UserResponse();
        userResponse.setEmail("update@example.com");

        when(userRepository.findByEmail(updatedRequest.getEmail())).thenReturn(Optional.of(user));
        when(userMapper.toEntity(updatedRequest)).thenReturn(user);
        when(passwordEncoder.encode(updatedRequest.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userMapper.toDto(savedUser)).thenReturn(userResponse);

        UserResponse result = userService.updateUser(updatedRequest);

        assertNotNull(result);
        assertEquals("update@example.com", result.getEmail());
    }

    @Test
    void testDeleteUser_Success() {
        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.deleteUser(userId);

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void testGetUserById_UserExists() {
        Long userId = 1L;

        User user = new User();
        user.setId(userId);
        user.setEmail("test@example.com");

        UserResponse userResponse = new UserResponse();
        userResponse.setEmail("test@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userResponse);

        Optional<UserResponse> result = userService.getUserById(userId);

        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getEmail());
    }

    @Test
    void testGetUserById_UserNotFound() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Optional<UserResponse> result = userService.getUserById(userId);

        assertFalse(result.isPresent());
    }
}