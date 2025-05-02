package com.library.library_management.service.impl;

import com.library.library_management.entity.User;
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

    @Override
    public User registerUser(User user) {
        boolean exists = userRepository.findByEmail(user.getEmail()).isPresent();
        if (exists) {
            throw new IllegalArgumentException("Bu e-posta adresi ile bir kullanıcı zaten mevcut.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, User updatedUser) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı."));
        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());
        user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        user.setRole(updatedUser.getRole());
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı."));
        userRepository.delete(user);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}

