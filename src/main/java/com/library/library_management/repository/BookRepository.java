package com.library.library_management.repository;

import com.library.library_management.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    @Query("""
        SELECT b FROM Book b
        WHERE (:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%')))
        AND (:author IS NULL OR LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%')))
        AND (:isbn IS NULL OR b.isbn = :isbn)
        AND (:genre IS NULL OR LOWER(b.genre) = LOWER(:genre))
    """)
    Page<Book> searchBooks(
            @Param("title") String title,
            @Param("author") String author,
            @Param("isbn") String isbn,
            @Param("genre") String genre,
            Pageable pageable
    );

    boolean existsByIsbn(String isbn);
}


