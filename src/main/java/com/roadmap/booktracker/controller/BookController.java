package com.roadmap.booktracker.controller;

import com.roadmap.booktracker.controller.annotation.ApiVersion;
import com.roadmap.booktracker.controller.filter.BookFilter;
import com.roadmap.booktracker.dto.book.BookResponse;
import com.roadmap.booktracker.dto.book.BookSummary;
import com.roadmap.booktracker.dto.book.CreateBookRequest;
import com.roadmap.booktracker.dto.book.UpdateBookRequest;
import com.roadmap.booktracker.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@ApiVersion("v1") @RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    @PostMapping("")
    public ResponseEntity<Void> createBook(@Valid @RequestBody CreateBookRequest request) {
        bookService.createBook(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("")
    public ResponseEntity<Page<BookSummary>> getAllBooks(@ParameterObject BookFilter filter, Pageable pageable) {
        return ResponseEntity.ok(bookService.getAllBooks(filter, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable UUID id) {
        return ResponseEntity.ok(bookService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateBook(@PathVariable UUID id, @Valid @RequestBody UpdateBookRequest request) {
        bookService.updateBook(request, id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
