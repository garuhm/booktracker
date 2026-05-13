package com.roadmap.booktracker.dto.book;

import java.util.List;
import java.util.UUID;

public record BookSummary(
        UUID id,
        String title,
        List<String> authors,
        int publishedYear,
        double averageRating,
        int reviewCount
) {}