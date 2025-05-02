package com.library.library_management.service.contract;

import com.library.library_management.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BookService {
    Book addBook(Book book);
    Optional<Book> getBookById(Long id);
    Page<Book> searchBooks(String title, String author, String isbn, String genre, Pageable pageable);
    Book updateBook(Long id, Book updatedBook);
    void deleteBook(Long id);
}

