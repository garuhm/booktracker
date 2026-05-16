package com.roadmap.booktracker.controller;

import com.roadmap.booktracker.controller.annotation.ApiVersion;
import com.roadmap.booktracker.dto.author.AuthorResponse;
import com.roadmap.booktracker.dto.author.AuthorSummary;
import com.roadmap.booktracker.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@ApiVersion("v1") @RequestMapping("/authors")
public class AuthorController {
    private final AuthorService authorService;

    @GetMapping("")
    public ResponseEntity<List<AuthorSummary>> getAllAuthors() {
        return ResponseEntity.ok(authorService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponse> getAuthorById(@PathVariable UUID id) {
        return ResponseEntity.ok(authorService.getById(id));
    }
}
