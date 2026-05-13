package com.roadmap.booktracker.dto.reading_list_entry;

import java.time.Instant;

public record UpdateReadingListEntryRequest(
        Integer pagesRead,
        Instant finishedAt
) {
}
