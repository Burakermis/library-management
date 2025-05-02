package com.library.library_management.controller;

import com.library.library_management.dto.requests.BookRequest;
import com.library.library_management.dto.responses.BookResponse;
import com.library.library_management.entity.Book;
import com.library.library_management.exception.ResourceNotFoundException;
import com.library.library_management.service.contract.BookService;
import com.library.library_management.mapper.BookMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Validated
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<BookResponse> addBook(@Valid @RequestBody BookRequest request) {
        Book book = bookService.addBook(BookMapper.toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(BookMapper.toDto(book));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookRequest request) {
        Book updatedBook = bookService.updateBook(id, BookMapper.toEntity(request));
        return ResponseEntity.ok(BookMapper.toDto(updatedBook));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBook(@PathVariable Long id) {
        Book book = bookService.getBookById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        return ResponseEntity.ok(BookMapper.toDto(book));
    }

    @GetMapping("/books")
    public ResponseEntity<Page<Book>> getBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) String genre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Book> books = bookService.getBooks(title, author, isbn, genre, pageable);
        return ResponseEntity.ok(books);
    }

    @GetMapping
    public ResponseEntity<Page<BookResponse>> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) String genre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> books = bookService.searchBooks(title, author, isbn, genre, pageable);
        Page<BookResponse> response = books.map(BookMapper::toDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok("Book deleted successfully");
    }


}


