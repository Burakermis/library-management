package com.library.library_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.library_management.dto.requests.CreateUserRequest;
import com.library.library_management.dto.requests.LoginUserRequest;
import com.library.library_management.dto.requests.UserRequest;
import com.library.library_management.dto.responses.UserResponse;
import com.library.library_management.entity.Role;
import com.library.library_management.service.contract.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public UserService userService() {
            return Mockito.mock(UserService.class);
        }
    }

    @Test
    void testRegisterUser() throws Exception {
        UserRequest request = new UserRequest("Test User", "test@example.com", "password123");
        UserResponse response = new UserResponse("Test User", "test@example.com");

        Mockito.when(userService.registerUser(any(UserRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v0/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void testLoginUser() throws Exception {
        LoginUserRequest request = new LoginUserRequest("test@example.com", "password123");
        String token = "mocked-jwt-token";

        Mockito.when(userService.login(anyString(), anyString())).thenReturn(token);

        mockMvc.perform(post("/api/v0/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("mocked-jwt-token"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateUser() throws Exception {
        CreateUserRequest request = new CreateUserRequest("Admin User", "admin@example.com", "password123", Role.ADMIN);
        UserResponse response = new UserResponse("Admin User", "admin@example.com");

        Mockito.when(userService.createUser(any(CreateUserRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v0/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Admin User"))
                .andExpect(jsonPath("$.email").value("admin@example.com"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateUser() throws Exception {
        UserRequest request = new UserRequest("Updated Name", "updated@example.com", "newpassword");
        UserResponse response = new UserResponse("Updated Name", "updated@example.com");

        Mockito.when(userService.updateUser(any(UserRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/v0/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())  // Beklenen 200 OK statüsü
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.email").value("updated@example.com"));
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteUser() throws Exception {
        Mockito.doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/v0/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully"));
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void testGetUserById() throws Exception {
        UserResponse response = new UserResponse("Test User", "test@example.com");

        Mockito.when(userService.getUserById(1L)).thenReturn(Optional.of(response));

        mockMvc.perform(get("/api/v0/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetUserById_NotFound() throws Exception {
        Mockito.when(userService.getUserById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v0/users/99"))
                .andExpect(status().isNotFound());
    }
}

