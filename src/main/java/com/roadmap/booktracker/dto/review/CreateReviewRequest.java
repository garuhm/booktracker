package com.roadmap.booktracker.dto.review;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

public record CreateReviewRequest(
        @NotBlank(message = "Review cannot be blank")
        String review,
        @Range(min = 1, max = 5, message = "Rating must be between 1 and 5")
        int rating
) {
}
