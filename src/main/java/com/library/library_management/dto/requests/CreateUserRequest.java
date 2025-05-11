package com.library.library_management.dto.requests;

import com.library.library_management.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CreateUserRequest {
    private String name;
    private String email;
    private String password;
    private Role role;
}
