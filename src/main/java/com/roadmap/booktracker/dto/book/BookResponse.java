package com.roadmap.booktracker.dto.book;

import com.roadmap.booktracker.dto.author.AuthorSummary;

import java.util.List;
import java.util.UUID;

public record BookResponse(
        UUID id,
        String title,
        String description,
        List<AuthorSummary> authors,
        int pages,
        int publishedYear,
        List<String> genres,
        int reviewCount,
        double averageRating
) {
}
