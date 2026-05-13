package com.roadmap.booktracker.dto.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

public record CreateBookRequest(
        @NotBlank(message = "Title cannot be blank")
        String title,
        String description,
        @NotEmpty(message = "Author IDs cannot be empty")
        List<@org.hibernate.validator.constraints.UUID UUID> authorIds,
        @NotBlank(message = "Pages cannot be blank")
        @Min(value = 1, message = "Pages must be greater than 0")
        int pages,
        @NotBlank(message = "Published year cannot be blank")
        int publishedYear,
        @NotEmpty(message = "Genre IDs cannot be empty")
        List<@org.hibernate.validator.constraints.UUID UUID> genreIds
) {
}
