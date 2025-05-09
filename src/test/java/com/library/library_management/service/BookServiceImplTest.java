package com.library.library_management.service;

import com.library.library_management.dto.requests.BookRequest;
import com.library.library_management.dto.responses.BookResponse;
import com.library.library_management.entity.Book;
import com.library.library_management.exception.BookAlreadyExistsException;
import com.library.library_management.exception.BookNotFoundException;
import com.library.library_management.mapper.BookMapper;
import com.library.library_management.repository.BookRepository;
import com.library.library_management.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddBook_Success() {
        BookRequest request = new BookRequest();
        request.setIsbn("1234567890");

        Book book = new Book();
        Book savedBook = new Book();
        savedBook.setId(1L);

        BookResponse response = new BookResponse();
        response.setId(1L);

        when(bookRepository.existsByIsbn(request.getIsbn())).thenReturn(false);
        when(bookMapper.toEntity(request)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(savedBook);
        when(bookMapper.toDto(savedBook)).thenReturn(response);

        BookResponse result = bookService.addBook(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void testAddBook_BookAlreadyExists() {
        BookRequest request = new BookRequest();
        request.setIsbn("1234567890");

        when(bookRepository.existsByIsbn(request.getIsbn())).thenReturn(true);

        assertThrows(BookAlreadyExistsException.class, () -> bookService.addBook(request));
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void testGetBookById_Success() {
        Long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);

        BookResponse response = new BookResponse();
        response.setId(bookId);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(response);

        BookResponse result = bookService.getBookById(bookId);

        assertNotNull(result);
        assertEquals(bookId, result.getId());
    }

    @Test
    void testGetBookById_BookNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.getBookById(1L));
    }

    @Test
    void testGetBooks_Success() {
        Pageable pageable = mock(Pageable.class);
        Book book = new Book();
        BookResponse response = new BookResponse();

        when(bookRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(new PageImpl<>(List.of(book)));
        when(bookMapper.toDto(book)).thenReturn(response);

        Page<BookResponse> result = bookService.getBooks(null, null, null, null, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testUpdateBook_Success() {
        Long bookId = 1L;
        BookRequest request = new BookRequest();
        Book existingBook = new Book();
        Book updatedBook = new Book();
        BookResponse response = new BookResponse();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookMapper.toEntity(request)).thenReturn(updatedBook);
        when(bookRepository.save(existingBook)).thenReturn(existingBook);
        when(bookMapper.toDto(existingBook)).thenReturn(response);

        BookResponse result = bookService.updateBook(bookId, request);

        assertNotNull(result);
        verify(bookRepository, times(1)).save(existingBook);
    }

    @Test
    void testUpdateBook_BookNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.updateBook(1L, new BookRequest()));
    }

    @Test
    void testDeleteBook_Success() {
        Long bookId = 1L;
        Book book = new Book();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        bookService.deleteBook(bookId);

        verify(bookRepository, times(1)).delete(book);
    }

    @Test
    void testDeleteBook_BookNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.deleteBook(1L));
    }
}