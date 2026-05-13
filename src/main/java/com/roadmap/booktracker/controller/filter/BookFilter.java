package com.roadmap.booktracker.controller.filter;

import java.util.UUID;

public record BookFilter(
        String title,
        Integer publishedYear,
        Integer publishedYearFrom,
        Integer publishedYearTo,
        Double minRating,
        UUID authorId,
        UUID genreId
) {}