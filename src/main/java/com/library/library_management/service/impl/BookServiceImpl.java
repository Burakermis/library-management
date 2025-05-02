package com.library.library_management.service.impl;

import com.library.library_management.entity.Book;
import com.library.library_management.exception.BookAlreadyExistsException;
import com.library.library_management.exception.BookNotFoundException;
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

    @Override
    public Book addBook(Book book) {
        checkIfBookExistsByIsbn(book.getIsbn());
        return bookRepository.save(book);
    }

    @Override
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    public Page<Book> getBooks(String title, String author, String isbn, String genre, Pageable pageable) {
        Specification<Book> spec = Specification.where(BookSpecification.hasTitle(title))
                .and(BookSpecification.hasAuthor(author))
                .and(BookSpecification.hasIsbn(isbn))
                .and(BookSpecification.hasGenre(genre));
        return bookRepository.findAll(spec, pageable);
    }

    @Override
    public Page<Book> searchBooks(String title, String author, String isbn, String genre, Pageable pageable) {
        Specification<Book> spec = createBookSpecification(title, author, isbn, genre);
        return bookRepository.findAll(spec, pageable);
    }

    @Override
    public Book updateBook(Long id, Book updatedBook) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Kitap bulunamadı!"));

        updateBookDetails(existingBook, updatedBook);

        return bookRepository.save(existingBook);
    }

    @Override
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Kitap bulunamadı!"));
        bookRepository.delete(book);
    }

    // Helper methods to improve readability and reuse
    private void checkIfBookExistsByIsbn(String isbn) {
        if (bookRepository.existsByIsbn(isbn)) {
            throw new BookAlreadyExistsException("Bu ISBN ile bir kitap zaten mevcut.");
        }
    }

    private Specification<Book> createBookSpecification(String title, String author, String isbn, String genre) {
        Specification<Book> spec = Specification.where(BookSpecification.hasTitle(title))
                .and(BookSpecification.hasAuthor(author))
                .and(BookSpecification.hasIsbn(isbn))
                .and(BookSpecification.hasGenre(genre));
        return spec;
    }

    private void updateBookDetails(Book existingBook, Book updatedBook) {
        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthor(updatedBook.getAuthor());
        existingBook.setIsbn(updatedBook.getIsbn());
        existingBook.setGenre(updatedBook.getGenre());
        existingBook.setPublicationDate(updatedBook.getPublicationDate());
    }
}

