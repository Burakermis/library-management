package com.library.library_management.service.contract;

import com.library.library_management.dto.requests.BookRequest;
import com.library.library_management.dto.responses.BookResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookResponse addBook(BookRequest request);
    BookResponse getBookById(Long id);
    Page<BookResponse> getBooks(String title, String author, String isbn, String genre, Pageable pageable);
    BookResponse updateBook(Long id, BookRequest request);
    void deleteBook(Long id);
}