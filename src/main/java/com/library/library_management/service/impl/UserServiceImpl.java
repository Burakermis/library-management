package com.library.library_management.service.impl;

import com.library.library_management.dto.requests.UserRequest;
import com.library.library_management.dto.responses.UserResponse;
import com.library.library_management.entity.User;
import com.library.library_management.mapper.UserMapper;
import com.library.library_management.repository.UserRepository;
import com.library.library_management.service.contract.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserResponse registerUser(UserRequest userRequest) {
        boolean exists = userRepository.findByEmail(userRequest.getEmail()).isPresent();
        if (exists) {
            throw new IllegalArgumentException("Bu e-posta adresi ile bir kullanıcı zaten mevcut.");
        }
        User user = userMapper.toEntity(userRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest updatedUser) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı."));
        user.setName(updatedUser.getUsername());
        user.setEmail(updatedUser.getEmail());
        user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı."));
        userRepository.delete(user);
    }

    @Override
    public Optional<UserResponse> getUserById(Long id) {
        return userRepository.findById(id).map(userMapper::toDto);
    }
}