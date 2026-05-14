package com.roadmap.booktracker.mapper;

import com.roadmap.booktracker.dto.book.BookResponse;
import com.roadmap.booktracker.dto.book.BookSummary;
import com.roadmap.booktracker.dto.book.CreateBookRequest;
import com.roadmap.booktracker.dto.book.UpdateBookRequest;
import com.roadmap.booktracker.entity.Book;
import com.roadmap.booktracker.entity.Genre;

import java.util.stream.Collectors;

public class BookMapper {
    public static Book createRequestToEntity(CreateBookRequest request) {
        return Book.builder()
                .title(request.title())
                .description(request.description())
                .publishedYear(request.publishedYear())
                .pages(request.pages())
                .build();
    }

    public static Book updateRequestToEntity(UpdateBookRequest request, Book book) {
        if(request.title() != null) book.setTitle(request.title());
        if(request.description() != null) book.setDescription(request.description());
        if(request.publishedYear() != null) book.setPublishedYear(request.publishedYear());

        return book;
    }

    public static BookResponse toResponse(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getDescription(),
                book.getAuthors()
                        .stream()
                        .map(AuthorMapper::toSummary)
                        .collect(Collectors.toList()),
                book.getPages(),
                book.getPublishedYear(),
                book.getGenres()
                        .stream()
                        .map(Genre::getName)
                        .collect(Collectors.toList()),
                book.getReviews().size(),
                book.getAverageRating()
        );
    }

    public static BookSummary toSummary(Book book) {
        return new BookSummary(
                book.getId(),
                book.getTitle(),
                book.getAuthors()
                        .stream()
                        .map(AuthorMapper::fullName)
                        .collect(Collectors.toList()),
                book.getPublishedYear(),
                book.getAverageRating(),
                book.getReviews().size()
        );
    }
}
