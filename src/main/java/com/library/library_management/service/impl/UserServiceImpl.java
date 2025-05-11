package com.library.library_management.service.impl;

import com.library.library_management.dto.requests.CreateUserRequest;
import com.library.library_management.dto.requests.UserRequest;
import com.library.library_management.dto.responses.UserResponse;
import com.library.library_management.entity.Role;
import com.library.library_management.entity.User;
import com.library.library_management.mapper.UserMapper;
import com.library.library_management.repository.UserRepository;
import com.library.library_management.security.JwtTokenProvider;
import com.library.library_management.service.contract.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public UserResponse registerUser(UserRequest userRequest) {
        log.info("Attempting to register user with email: {}", userRequest.getEmail());
        boolean exists = userRepository.findByEmail(userRequest.getEmail()).isPresent();
        if (exists) {
            log.warn("Registration failed. Email already in use: {}", userRequest.getEmail());
            throw new IllegalArgumentException("Bu e-posta adresi ile bir kullanıcı zaten mevcut.");
        }
        User user = userMapper.toEntity(userRequest);
        user.setRole(Role.READER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        log.info("User registered successfully with ID: {}", savedUser.getId());
        return userMapper.toDto(savedUser);
    }

    @Override
    public UserResponse createUser(CreateUserRequest createUserRequest) {
        log.info("Attempting to create user with email: {}", createUserRequest.getEmail());
        boolean exists = userRepository.findByEmail(createUserRequest.getEmail()).isPresent();
        if (exists) {
            log.warn("Create failed. Email already in use: {}", createUserRequest.getEmail());
            throw new IllegalArgumentException("Bu e-posta adresi ile bir kullanıcı zaten mevcut.");
        }
        User user = userMapper.toEntity(createUserRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        log.info("User created successfully with ID: {}", savedUser.getId());
        return userMapper.toDto(savedUser);
    }

    @Override
    public String login(String email, String password) {
        log.info("Attempting to log in user with email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Login failed. User not found for email: {}", email);
                    return new IllegalArgumentException("Geçersiz e-posta");
                });

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Geçersiz şifre.");
        }

        String token = jwtTokenProvider.generateToken(user.getEmail(), user.getRole());
        log.info("Login successful for user with email: {}", email);

        return token;
    }

    @Override
    public UserResponse updateUser(UserRequest updatedUser) {
        log.info("Updating user with Email: {}", updatedUser.getEmail());
        boolean exists = userRepository.findByEmail(updatedUser.getEmail()).isPresent();
        if (exists) {
            User user = userMapper.toEntity(updatedUser);
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            User savedUser = userRepository.save(user);
            log.info("User with Email: {} updated successfully", updatedUser.getEmail());
            return userMapper.toDto(savedUser);
        } else {
            log.warn("Registration failed. Email already in use: {}", updatedUser.getEmail());
            throw new IllegalArgumentException("Bu e-posta adresi ile bir kullanıcı değil.");
        }
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Delete failed. User not found for ID: {}", id);
                    return new RuntimeException("Kullanıcı bulunamadı.");
                });
        userRepository.delete(user);
        log.info("User with ID: {} deleted successfully", id);
    }

    @Override
    public Optional<UserResponse> getUserById(Long id) {
        log.info("Fetching user with ID: {}", id);
        return userRepository.findById(id)
                .map(user -> {
                    log.debug("User found with ID: {}", id);
                    return userMapper.toDto(user);
                });
    }
}
