package com.roadmap.booktracker.mapper;

import com.roadmap.booktracker.dto.genre.GenreResponse;
import com.roadmap.booktracker.dto.genre.GenreStats;
import com.roadmap.booktracker.dto.projection.GenreStatsProjection;
import com.roadmap.booktracker.entity.Genre;

import java.util.stream.Collectors;

public class GenreMapper {
    public static GenreResponse toResponse (Genre genre) {
        return new GenreResponse(
                genre.getId(),
                genre.getName(),
                genre.getBooks()
                        .stream()
                        .map(BookMapper::toSummary)
                        .collect(Collectors.toList())
                );
    }

    public static GenreStats toGenreStats(GenreStatsProjection genreStats) {
        return new GenreStats(
                genreStats.getName(),
                genreStats.getBookCount(),
                genreStats.getAverageRating()
        );
    }
}
