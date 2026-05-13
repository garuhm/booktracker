package com.roadmap.booktracker.service;

import com.roadmap.booktracker.dto.book.CreateBookRequest;
import com.roadmap.booktracker.entity.Author;
import com.roadmap.booktracker.entity.Book;
import com.roadmap.booktracker.entity.Genre;
import com.roadmap.booktracker.mapper.BookMapper;
import com.roadmap.booktracker.repo.AuthorRepository;
import com.roadmap.booktracker.repo.BookRepository;
import com.roadmap.booktracker.repo.GenreRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    @Transactional
    public void createBook(CreateBookRequest request) {
        Book book = BookMapper.createRequestToEntity(request);

        Set<UUID> authorIds = new HashSet<>(request.authorIds());
        Set<UUID> genreIds = new HashSet<>(request.genreIds());

        Set<Author> authors = Set.copyOf(authorRepository.findAllById(authorIds));
        Set<Genre> genres = Set.copyOf(genreRepository.findAllById(genreIds));

        if (authors.size() != authorIds.size()) {
            throw new EntityNotFoundException("One or more authors not found");
        }
        if (genres.size() != genreIds.size()) {
            throw new EntityNotFoundException("One or more genres not found");
        }

        book.setAuthors(authors);
        book.setGenres(genres);

        bookRepository.save(book);
    }
}
