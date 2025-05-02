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

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookResponse addBook(BookRequest request) {
        checkIfBookExistsByIsbn(request.getIsbn());
        Book book = bookMapper.toEntity(request);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toDto(savedBook);
    }

    @Override
    public BookResponse getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Kitap bulunamadı!"));
        return bookMapper.toDto(book);
    }

    @Override
    public Page<BookResponse> getBooks(String title, String author, String isbn, String genre, Pageable pageable) {
        Specification<Book> spec = Specification.where(BookSpecification.hasTitle(title))
                .and(BookSpecification.hasAuthor(author))
                .and(BookSpecification.hasIsbn(isbn))
                .and(BookSpecification.hasGenre(genre));
        Page<Book> books = bookRepository.findAll(spec, pageable);
        return books.map(bookMapper::toDto);
    }

    @Override
    public BookResponse updateBook(Long id, BookRequest request) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Kitap bulunamadı!"));

        Book updatedBook = bookMapper.toEntity(request);
        updateBookDetails(existingBook, updatedBook);

        Book savedBook = bookRepository.save(existingBook);
        return bookMapper.toDto(savedBook);
    }

    @Override
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Kitap bulunamadı!"));
        bookRepository.delete(book);
    }

    // Helper methods
    private void checkIfBookExistsByIsbn(String isbn) {
        if (bookRepository.existsByIsbn(isbn)) {
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