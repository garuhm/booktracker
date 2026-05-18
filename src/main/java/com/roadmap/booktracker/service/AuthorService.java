package com.roadmap.booktracker.service;

import com.roadmap.booktracker.dto.author.AuthorResponse;
import com.roadmap.booktracker.dto.author.AuthorSummary;
import com.roadmap.booktracker.entity.Author;
import com.roadmap.booktracker.mapper.AuthorMapper;
import com.roadmap.booktracker.repo.AuthorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public List<AuthorSummary> getAll() {
        return authorRepository.findAllWithBookCount()
                .stream()
                .map(AuthorMapper::toSummary)
                .collect(Collectors.toList());
    }

    public AuthorResponse getById(UUID id) {
        Author author = authorRepository.findByIdWithBooksAndAuthors(id)
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id: " + id));
        return AuthorMapper.toResponse(author);
    }
}
