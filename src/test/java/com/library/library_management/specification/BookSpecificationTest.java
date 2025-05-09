package com.library.library_management.specification;

import com.library.library_management.entity.Book;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookSpecificationTest {

    @Test
    void testHasTitle() {
        Root<Book> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder criteriaBuilder = mock(CriteriaBuilder.class);

        when(criteriaBuilder.conjunction()).thenReturn(null);

        Specification<Book> spec = BookSpecification.hasTitle("Test Title");
        assertNotNull(spec);
        spec.toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder, times(1)).like(any(), eq("%test title%"));
    }

    @Test
    void testHasAuthor() {
        Root<Book> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder criteriaBuilder = mock(CriteriaBuilder.class);

        when(criteriaBuilder.conjunction()).thenReturn(null);

        Specification<Book> spec = BookSpecification.hasAuthor("Test Author");
        assertNotNull(spec);
        spec.toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder, times(1)).like(any(), eq("%test author%"));
    }

    @Test
    void testHasIsbn() {
        Root<Book> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder criteriaBuilder = mock(CriteriaBuilder.class);

        when(criteriaBuilder.conjunction()).thenReturn(null);

        Specification<Book> spec = BookSpecification.hasIsbn("1234567890");
        assertNotNull(spec);
        spec.toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder, times(1)).equal(any(), eq("1234567890"));
    }

    @Test
    void testHasGenre() {
        Root<Book> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder criteriaBuilder = mock(CriteriaBuilder.class);

        when(criteriaBuilder.conjunction()).thenReturn(null);

        Specification<Book> spec = BookSpecification.hasGenre("Fiction");
        assertNotNull(spec);
        spec.toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder, times(1)).equal(any(), eq("fiction"));
    }
}