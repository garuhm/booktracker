package com.roadmap.booktracker.mapper;

import com.roadmap.booktracker.dto.author.AuthorSummary;
import com.roadmap.booktracker.dto.projection.AuthorSummaryProjection;
import com.roadmap.booktracker.entity.Author;

public class AuthorMapper {
    public static AuthorSummary toSummary(Author author) {
        return new AuthorSummary(
                author.getFirstName(),
                author.getLastName(),
                (long) author.getBooks().size()
        );
    }

    public static AuthorSummary toSummary(AuthorSummaryProjection author) {
        return new AuthorSummary(
                author.getFirstName(),
                author.getLastName(),
                author.getBookCount()
        );
    }

    public static String fullName(Author author) {
        return author.getFirstName() + " " + author.getLastName();
    }
}
