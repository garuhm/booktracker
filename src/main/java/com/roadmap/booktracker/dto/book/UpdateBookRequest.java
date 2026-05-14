package com.roadmap.booktracker.dto.book;

import java.util.List;
import java.util.UUID;

public record UpdateBookRequest(
        String title,
        String description,
        Integer publishedYear,
        List<UUID> authorIds,
        List<UUID> genreIds
) {
}
