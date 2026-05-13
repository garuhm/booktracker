package com.roadmap.booktracker.dto.reading_list_entry;

import jakarta.validation.constraints.NotBlank;

import java.time.Instant;
import java.util.UUID;

public record CreateReadingListEntryRequest(
        @NotBlank(message = "Book ID cannot be blank")
        UUID bookId,
        @NotBlank(message = "Pages read cannot be blank")
        int pagesRead,
        @NotBlank(message = "Started at cannot be blank")
        Instant startedAt
) {
}
