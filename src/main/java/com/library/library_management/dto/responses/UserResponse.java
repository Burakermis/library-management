package com.library.library_management.dto.responses;

import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String name;
    private String email;
}