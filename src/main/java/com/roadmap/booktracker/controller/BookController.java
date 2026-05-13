package com.roadmap.booktracker.controller;

import com.roadmap.booktracker.controller.annotation.ApiVersion;
import com.roadmap.booktracker.controller.filter.BookFilter;
import com.roadmap.booktracker.dto.book.BookSummary;
import com.roadmap.booktracker.dto.book.CreateBookRequest;
import com.roadmap.booktracker.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
