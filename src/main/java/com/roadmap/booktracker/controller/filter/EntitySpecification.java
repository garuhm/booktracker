package com.roadmap.booktracker.controller.filter;

import com.roadmap.booktracker.entity.Author;
import com.roadmap.booktracker.entity.Book;
import org.springframework.data.jpa.domain.Specification;

public class EntitySpecification {
    public static Specification<Book> fromFilter(BookFilter filter) {
        return Specification
//                sets the return type to book
                .<Book>where(like("title", filter.title()))
                .and(equal("publishedYear", filter.publishedYear()))
                .and(between("publishedYear", filter.publishedYearFrom(), filter.publishedYearTo()))
                .and(between("averageRating", filter.minRating(), null))
                .and(joinEqual("authors", "id", filter.authorId()))
                .and(joinEqual("genres", "id", filter.genreId()));

    }

    private static <T> Specification<T> like(String field, String value) {
        return (root, query, cb) -> value == null ? null
                : cb.like(cb.lower(root.get(field)), "%" + value.toLowerCase() + "%");
    }

    private static <T> Specification<T> equal(String field, Object value) {
        return (root, query, cb) -> value == null ? null
                : cb.equal(root.get(field), value);
    }

    private static <T, V extends Comparable<V>> Specification<T> between(String field, V from, V to) {
        return (root, query, cb) -> {
            if (from == null && to == null) return null;
//            if from is null to is the limit
            if (from == null) return cb.lessThanOrEqualTo(root.get(field), to);
//            is to is null its everything from from until the end
            if (to == null) return cb.greaterThanOrEqualTo(root.get(field), from);
            return cb.between(root.get(field), from, to);
        };
    }

//     join is another table
    private static <T> Specification<T> joinEqual(String join, String field, Object value) {
        return (root, query, cb) -> value == null ? null
                : cb.equal(root.join(join).get(field), value);
    }
}
