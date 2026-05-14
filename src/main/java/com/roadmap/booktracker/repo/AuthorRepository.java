package com.roadmap.booktracker.repo;

import com.roadmap.booktracker.dto.projection.AuthorSummaryProjection;
import com.roadmap.booktracker.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface AuthorRepository extends JpaRepository<Author, UUID> {
    @Query("SELECT a.firstName as firstName, " +
            "a.lastName as lastName, " +
            "COUNT(b) as bookCount " +
            "FROM Author a JOIN a.books b GROUP BY a.firstName, a.lastName")
    List<AuthorSummaryProjection> findAllWithBookCount();
}
