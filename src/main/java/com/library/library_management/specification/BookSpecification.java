package com.library.library_management.specification;

import com.library.library_management.entity.Book;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {

    // Title filtreleme
    public static Specification<Book> hasTitle(String title) {
        return (root, query, criteriaBuilder) ->
                title == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    // Author filtreleme
    public static Specification<Book> hasAuthor(String author) {
        return (root, query, criteriaBuilder) ->
                author == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("author")), "%" + author.toLowerCase() + "%");
    }

    // ISBN filtreleme
    public static Specification<Book> hasIsbn(String isbn) {
        return (root, query, criteriaBuilder) ->
                isbn == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.equal(root.get("isbn"), isbn);
    }

    // Genre filtreleme
    public static Specification<Book> hasGenre(String genre) {
        return (root, query, criteriaBuilder) ->
                genre == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.equal(criteriaBuilder.lower(root.get("genre")), genre.toLowerCase());
    }
}

