package com.roadmap.booktracker.dto.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record CreateBookRequest(
        @NotBlank(message = "Title cannot be blank")
        String title,
        String description,
        @NotEmpty(message = "Author IDs cannot be empty")
        List<UUID> authorIds,
        @NotNull(message = "Pages cannot be blank")
        @Min(value = 1, message = "Pages must be greater than 0")
        Integer pages,
        @NotNull(message = "Published year cannot be blank")
        Integer publishedYear,
        @NotEmpty(message = "Genre IDs cannot be empty")
        List<UUID> genreIds
) {
}
