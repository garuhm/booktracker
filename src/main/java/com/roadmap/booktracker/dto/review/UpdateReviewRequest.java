package com.roadmap.booktracker.dto.review;

public record UpdateReviewRequest(
        String review,
        Integer rating
) {
}
