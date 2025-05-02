package com.library.library_management.dto.requests;

import lombok.Data;

@Data
public class UserRequest {
    private String username;
    private String email;
    private String password;
}