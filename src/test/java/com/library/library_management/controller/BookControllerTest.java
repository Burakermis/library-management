package com.library.library_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.library_management.dto.requests.BookRequest;
import com.library.library_management.dto.responses.BookResponse;
import com.library.library_management.service.contract.BookService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public BookService bookService() {
            return Mockito.mock(BookService.class);
        }
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void testAddBook() throws Exception {
        BookRequest request = BookRequest.builder()
                .title("Title")
                .author("Author")
                .isbn("1234567890")
                .publicationDate(LocalDate.of(2024, 1, 1))
                .genre("Genre")
                .build();

        BookResponse response = BookResponse.builder()
                .id(1L)
                .title("Title")
                .author("Author")
                .isbn("1234567890")
                .publicationDate(LocalDate.of(2024, 1, 1))
                .genre("Genre")
                .available(true)
                .build();


        Mockito.when(bookService.addBook(any(BookRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v0/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void testUpdateBook() throws Exception {
        BookRequest request = BookRequest.builder()
                .title("Updated Title")
                .author("Author")
                .isbn("1234567890")
                .publicationDate(LocalDate.of(2024, 1, 1))
                .genre("Genre")
                .build();

        BookResponse response = BookResponse.builder()
                .id(1L)
                .title("Updated Title")
                .author("Author")
                .isbn("1234567890")
                .publicationDate(LocalDate.of(2024, 1, 1))
                .genre("Genre")
                .available(true)
                .build();


        Mockito.when(bookService.updateBook(eq(1L), any(BookRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/v0/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void testGetBook() throws Exception {
        BookResponse response = BookResponse.builder()
                .id(1L)
                .title("Title")
                .author("Author")
                .isbn("1234567890")
                .publicationDate(LocalDate.of(2024, 1, 1))
                .genre("Genre")
                .available(true)
                .build();


        Mockito.when(bookService.getBookById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v0/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void testSearchBooks() throws Exception {
        BookResponse response = BookResponse.builder()
                .id(1L)
                .title("Title")
                .author("Author")
                .isbn("1234567890")
                .publicationDate(LocalDate.of(2024, 1, 1))
                .genre("Genre")
                .available(true)
                .build();

        Pageable pageable = PageRequest.of(0, 10, Sort.by("title"));
        Page<BookResponse> page = new PageImpl<>(Collections.singletonList(response), pageable, 1);

        Mockito.when(bookService.getBooks(any(), any(), any(), any(), any())).thenReturn(page);

        mockMvc.perform(get("/api/v0/books")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "title"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L));
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void testDeleteBook() throws Exception {
        mockMvc.perform(delete("/api/v0/books/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Book deleted successfully"));

        Mockito.verify(bookService).deleteBook(1L);
    }
}

