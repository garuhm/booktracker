package com.roadmap.booktracker.entity.embedded;

import com.roadmap.booktracker.entity.enums.ReadingStatus;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.Instant;

@Embeddable
public class ReadingEntry {
    @Enumerated(EnumType.STRING)
    private ReadingStatus status;

    private int pagesRead;

    private Instant startedAt;
    private Instant finishedAt;
}
