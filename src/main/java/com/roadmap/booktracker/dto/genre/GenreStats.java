package com.roadmap.booktracker.dto.genre;

public record GenreStats(
        String genreName,
        Long bookCount,
        Double averageRating
) {
}
