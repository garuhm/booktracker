package com.roadmap.booktracker.controller.filter;

import com.roadmap.booktracker.entity.Book;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {
    public static Specification<Book> fromFilter(BookFilter filter) {
        return Specification
                .where(like("title", filter.title()))
                .and(equal("publishedYear", filter.publishedYear()))
                .and(between("publishedYear", filter.publishedYearFrom(), filter.publishedYearTo()))
                .and(between("averageRating", filter.minRating(), null))
                .and(joinEqual("authors", "id", filter.authorId()))
                .and(joinEqual("genres", "id", filter.genreId()));
    }

    private static Specification<Book> like(String field, String value) {
        return (root, query, cb) -> value == null ? null
                : cb.like(cb.lower(root.get(field)), "%" + value.toLowerCase() + "%");
    }

    private static Specification<Book> equal(String field, Object value) {
        return (root, query, cb) -> value == null ? null
                : cb.equal(root.get(field), value);
    }

    private static Specification<Book> between(String field, Comparable from, Comparable to) {
        return (root, query, cb) -> {
            if (from == null && to == null) return null;
            if (from == null) return cb.lessThanOrEqualTo(root.get(field), to);
            if (to == null) return cb.greaterThanOrEqualTo(root.get(field), from);
            return cb.between(root.get(field), from, to);
        };
    }

//    join = other table
    private static Specification<Book> joinEqual(String join, String field, Object value) {
        return (root, query, cb) -> value == null ? null
                : cb.equal(root.join(join).get(field), value);
    }
}
