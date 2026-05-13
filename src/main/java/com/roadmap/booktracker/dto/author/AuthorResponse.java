package com.roadmap.booktracker.dto.author;

import com.roadmap.booktracker.dto.book.BookSummary;

import java.util.List;
import java.util.UUID;

public record AuthorResponse(
        UUID id,
        String firstName,
        String lastName,
        List<BookSummary> books
) {
}
