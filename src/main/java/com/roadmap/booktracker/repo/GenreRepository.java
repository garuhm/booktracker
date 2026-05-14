package com.roadmap.booktracker.repo;

import com.roadmap.booktracker.dto.projection.GenreStatsProjection;
import com.roadmap.booktracker.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface GenreRepository extends JpaRepository<Genre, UUID> {
    @Query("SELECT g.name as name, " +
            "COUNT(b) as bookCount, " +
            "AVG(b.averageRating) as averageRating " +
            "FROM Genre g JOIN g.books b GROUP BY g.name")
    List<GenreStatsProjection> findGenreStats();
}
