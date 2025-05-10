package com.library.library_management.service.contract;

import com.library.library_management.dto.requests.UserRequest;
import com.library.library_management.dto.responses.UserResponse;

import java.util.Optional;

public interface UserService {
    UserResponse registerUser(UserRequest userRequest);
    UserResponse updateUser(UserRequest updatedUser);
    void deleteUser(Long id);
    Optional<UserResponse> getUserById(Long id);
}