package com.roadmap.booktracker.service;

import com.roadmap.booktracker.controller.filter.BookFilter;
import com.roadmap.booktracker.controller.filter.EntitySpecification;
import com.roadmap.booktracker.dto.book.BookResponse;
import com.roadmap.booktracker.dto.book.BookSummary;
import com.roadmap.booktracker.dto.book.CreateBookRequest;
import com.roadmap.booktracker.dto.book.UpdateBookRequest;
import com.roadmap.booktracker.entity.Author;
import com.roadmap.booktracker.entity.Book;
import com.roadmap.booktracker.entity.Genre;
import com.roadmap.booktracker.mapper.BookMapper;
import com.roadmap.booktracker.repo.AuthorRepository;
import com.roadmap.booktracker.repo.BookRepository;
import com.roadmap.booktracker.repo.GenreRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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
    public void create(CreateBookRequest request) {
        Set<Author> authors = resolveEntities(authorRepository, request.authorIds(), "author");
        Set<Genre> genres = resolveEntities(genreRepository, request.genreIds(), "genre");
        Book book = BookMapper.createRequestToEntity(request, authors, genres);

        bookRepository.saveAndFlush(book);
    }

    public Page<BookSummary> getAll(BookFilter filter, Pageable pageable) {
        return bookRepository.findAll(EntitySpecification.fromFilter(filter), pageable)
                .map(BookMapper::toSummary);
    }

    public BookResponse getById(UUID id) {
        return bookRepository.findById(id)
                .map(BookMapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + id));
    }

    @Transactional
    public void update(UpdateBookRequest request, UUID id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + id));

        Set<Author> authors = resolveEntities(authorRepository, request.authorIds(), "author");
        Set<Genre> genres = resolveEntities(genreRepository, request.genreIds(), "genre");
        BookMapper.updateRequestToEntity(request, book, authors, genres);

        bookRepository.saveAndFlush(book);
    }

    private <T, ID> Set<T> resolveEntities(JpaRepository<T, ID> repository, List<ID> ids, String entityName) {
        if (ids == null) return Set.of();

        Set<ID> uniqueIds = new HashSet<>(ids);

        List<T> found = repository.findAllById(uniqueIds);
        if (found.size() != uniqueIds.size()) {
            throw new EntityNotFoundException("One or more " + entityName + " not found");
        }

        return new HashSet<>(found);
    }
}
