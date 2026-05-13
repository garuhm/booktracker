package com.roadmap.booktracker.mapper;

import com.roadmap.booktracker.dto.author.AuthorSummary;
import com.roadmap.booktracker.entity.Author;

public class AuthorMapper {
    public static AuthorSummary toSummary(Author author) {
        return new AuthorSummary(
                author.getFirstName(),
                author.getLastName()
        );
    }

    public static String fullName(Author author) {
        return author.getFirstName() + " " + author.getLastName();
    }
}
