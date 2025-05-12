package com.library.library_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.library_management.dto.responses.BorrowingResponse;
import com.library.library_management.service.contract.BorrowingService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest
@AutoConfigureMockMvc
class BorrowingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BorrowingService borrowingService;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public BorrowingService borrowingService() {
            return Mockito.mock(BorrowingService.class);
        }
    }

    @Test
    @WithMockUser(roles = "READER")
    void testBorrowBook() throws Exception {
        BorrowingResponse response = BorrowingResponse.builder()
                .id(1L)
                .userId(1L)
                .bookId(1L)
                .borrowDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(14))
                .returned(false)
                .build();

        Mockito.when(borrowingService.borrowBook(anyLong(), anyLong())).thenReturn(response);

        mockMvc.perform(post("/api/v0/borrowings/borrow")
                        .param("userId", "1")
                        .param("bookId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.returned").value(false));
    }

    @Test
    @WithMockUser(roles = "READER")
    void testReturnBook() throws Exception {
        BorrowingResponse response = BorrowingResponse.builder()
                .id(1L)
                .userId(1L)
                .bookId(1L)
                .borrowDate(LocalDate.now().minusDays(10))
                .dueDate(LocalDate.now().plusDays(4))
                .returnDate(LocalDate.now())
                .returned(true)
                .build();

        Mockito.when(borrowingService.returnBook(1L)).thenReturn(response);

        mockMvc.perform(put("/api/v0/borrowings/return/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.returnDate").isNotEmpty())
                .andExpect(jsonPath("$.returned").value(true));
    }

    @Test
    @WithMockUser(roles = "READER")
    void testUserBorrowingHistory() throws Exception {
        List<BorrowingResponse> responses = List.of(
                BorrowingResponse.builder()
                        .id(1L)
                        .userId(1L)
                        .bookId(1L)
                        .borrowDate(LocalDate.now().minusDays(10))
                        .dueDate(LocalDate.now().minusDays(3))
                        .returnDate(LocalDate.now().minusDays(2))
                        .returned(true)
                        .build()
        );

        Mockito.when(borrowingService.getUserHistory(1L)).thenReturn(responses);

        mockMvc.perform(get("/api/v0/borrowings/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].returned").value(true));
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void testGetOverdueBooks() throws Exception {
        List<BorrowingResponse> responses = List.of(
                BorrowingResponse.builder()
                        .id(2L)
                        .userId(2L)
                        .bookId(2L)
                        .borrowDate(LocalDate.now().minusDays(20))
                        .dueDate(LocalDate.now().minusDays(6))
                        .returnDate(null)
                        .returned(false)
                        .build()
        );

        Mockito.when(borrowingService.getOverdueBooks()).thenReturn(responses);

        mockMvc.perform(get("/api/v0/borrowings/overdue"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].returned").value(false))
                .andExpect(jsonPath("$[0].dueDate").isNotEmpty());
    }
}
