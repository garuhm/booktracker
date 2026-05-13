package com.roadmap.booktracker.dto.review;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

public record CreateReviewRequest(
        @NotBlank(message = "Review cannot be blank")
        String review,
        @NotNull(message = "Rating cannot be missing")
        @Range(min = 1, max = 5, message = "Rating must be between 1 and 5")
        Integer rating
) {
}
