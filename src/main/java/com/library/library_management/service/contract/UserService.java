package com.library.library_management.service.contract;

import com.library.library_management.entity.User;

import java.util.Optional;

public interface UserService {
    User registerUser(User user);
    User updateUser(Long id, User updatedUser);
    void deleteUser(Long id);
    Optional<User> getUserById(Long id);
    Optional<User> getUserByEmail(String email);
}

