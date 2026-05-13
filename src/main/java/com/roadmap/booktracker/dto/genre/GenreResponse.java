package com.roadmap.booktracker.dto.genre;

import com.roadmap.booktracker.dto.book.BookSummary;

import java.util.List;
import java.util.UUID;

public record GenreResponse(
        UUID id,
        String name,
        List<BookSummary> books
) {
}
