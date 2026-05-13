package com.roadmap.booktracker.dto.reading_list_entry;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record CreateReadingListEntryRequest(
        @NotNull(message = "Book ID cannot be blank")
        UUID bookId,
        @NotNull(message = "Pages read cannot be blank")
        Integer pagesRead,
        @NotNull(message = "Started at cannot be blank")
        Instant startedAt
) {
}
