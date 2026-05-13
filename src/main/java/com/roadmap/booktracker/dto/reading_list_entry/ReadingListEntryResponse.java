package com.roadmap.booktracker.dto.reading_list_entry;

import java.time.Instant;
import java.util.UUID;

public record ReadingListEntryResponse(
        UUID id,
        String bookTitle,
        String status,
        int pagesRead,
        Instant startedAt,
        Instant finishedAt
) {
}
