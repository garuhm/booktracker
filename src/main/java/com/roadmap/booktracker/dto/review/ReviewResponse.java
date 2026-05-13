package com.roadmap.booktracker.dto.review;

import java.time.Instant;
import java.util.UUID;

public record ReviewResponse(
        UUID id,
        int rating,
        String review,
        Instant createdAt
) {}