package com.roadmap.booktracker.mapper;

import com.roadmap.booktracker.dto.genre.GenreResponse;
import com.roadmap.booktracker.entity.Genre;

import java.util.stream.Collectors;

public class GenreMapper {
    public static GenreResponse toResponse (Genre genre) {
        return new GenreResponse(
                genre.getId(),
                genre.getName(),
                genre.getBooks()
                        .stream()
                        .map(BookMapper::toSummary)
                        .collect(Collectors.toList())
                );
    }
}
