package com.roadmap.booktracker.mapper;

import com.roadmap.booktracker.dto.author.AuthorResponse;
import com.roadmap.booktracker.dto.author.AuthorSummary;
import com.roadmap.booktracker.dto.projection.AuthorSummaryProjection;
import com.roadmap.booktracker.entity.Author;
import com.roadmap.booktracker.entity.Book;

import java.util.stream.Collectors;

public class AuthorMapper {
    public static AuthorResponse toResponse(Author author) {
        return new AuthorResponse(
                author.getId(),
                author.getFirstName(),
                author.getLastName(),
                author.getBooks().stream()
                        .map(BookMapper::toSummary)
                        .collect(Collectors.toSet())
        );
    }


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
