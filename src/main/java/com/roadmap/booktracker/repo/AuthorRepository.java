package com.roadmap.booktracker.repo;

import com.roadmap.booktracker.dto.projection.AuthorSummaryProjection;
import com.roadmap.booktracker.entity.Author;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthorRepository extends
        JpaRepository<Author, UUID>,
        JpaSpecificationExecutor<Author> {
    @Query("SELECT DISTINCT a FROM Author a " +
            "JOIN FETCH a.books b " +
            "LEFT JOIN FETCH b.authors " +
            "LEFT JOIN FETCH b.reviews " +
            "WHERE a.id = :id")
    Optional<Author> findByIdWithBooksAndAuthors(@Param("id") UUID id);

    @Query("SELECT a.firstName as firstName, " +
            "a.lastName as lastName, " +
            "COUNT(b) as bookCount " +
            "FROM Author a JOIN a.books b GROUP BY a.firstName, a.lastName")
    List<AuthorSummaryProjection> findAllWithBookCount();
}
