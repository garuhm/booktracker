package com.roadmap.booktracker.dto.author;


import java.util.UUID;

public record AuthorResponse(
        UUID id,
        String firstName,
        String lastName,
        Integer bookCount
) {
}
