package com.library.library_management.service.impl;

import com.library.library_management.dto.requests.BookRequest;
import com.library.library_management.dto.responses.BookResponse;
import com.library.library_management.entity.Book;
import com.library.library_management.exception.BookAlreadyExistsException;
import com.library.library_management.exception.BookNotFoundException;
import com.library.library_management.mapper.BookMapper;
import com.library.library_management.repository.BookRepository;
import com.library.library_management.service.contract.BookService;
import com.library.library_management.specification.BookSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookResponse addBook(BookRequest request) {
        log.info("Adding new book with ISBN: {}", request.getIsbn());
        checkIfBookExistsByIsbn(request.getIsbn());

        Book book = bookMapper.toEntity(request);
        Book savedBook = bookRepository.save(book);

        log.info("Book added successfully with ID: {}", savedBook.getId());
        return bookMapper.toDto(savedBook);
    }

    @Override
    public BookResponse getBookById(Long id) {
        log.info("Fetching book with ID: {}", id);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Book with ID {} not found", id);
                    return new BookNotFoundException("Kitap bulunamadı!");
                });
        return bookMapper.toDto(book);
    }

    @Override
    public Page<BookResponse> getBooks(String title, String author, String isbn, String genre, Pageable pageable) {
        log.info("Searching books with filters - title: {}, author: {}, isbn: {}, genre: {}", title, author, isbn, genre);
        Specification<Book> spec = Specification.where(BookSpecification.hasTitle(title))
                .and(BookSpecification.hasAuthor(author))
                .and(BookSpecification.hasIsbn(isbn))
                .and(BookSpecification.hasGenre(genre));

        Page<Book> books = bookRepository.findAll(spec, pageable);
        log.debug("Found {} books matching filters", books.getTotalElements());
        return books.map(bookMapper::toDto);
    }

    @Override
    public BookResponse updateBook(Long id, BookRequest request) {
        log.info("Updating book with ID: {}", id);
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Book with ID {} not found", id);
                    return new BookNotFoundException("Kitap bulunamadı!");
                });

        Book updatedBook = bookMapper.toEntity(request);
        updateBookDetails(existingBook, updatedBook);

        Book savedBook = bookRepository.save(existingBook);
        log.info("Book with ID {} updated successfully", id);
        return bookMapper.toDto(savedBook);
    }

    @Override
    public void deleteBook(Long id) {
        log.info("Deleting book with ID: {}", id);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Book with ID {} not found", id);
                    return new BookNotFoundException("Kitap bulunamadı!");
                });

        bookRepository.delete(book);
        log.info("Book with ID {} deleted successfully", id);
    }

    private void checkIfBookExistsByIsbn(String isbn) {
        if (bookRepository.existsByIsbn(isbn)) {
            log.warn("Attempted to add book with existing ISBN: {}", isbn);
            throw new BookAlreadyExistsException("Bu ISBN ile bir kitap zaten mevcut.");
        }
    }

    private void updateBookDetails(Book existingBook, Book updatedBook) {
        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthor(updatedBook.getAuthor());
        existingBook.setIsbn(updatedBook.getIsbn());
        existingBook.setGenre(updatedBook.getGenre());
        existingBook.setPublicationDate(updatedBook.getPublicationDate());
    }
}
