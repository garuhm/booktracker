package com.roadmap.booktracker.controller;

import com.roadmap.booktracker.controller.annotation.ApiVersion;
import com.roadmap.booktracker.dto.book.CreateBookRequest;
import com.roadmap.booktracker.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequiredArgsConstructor
@ApiVersion("v1") @RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    @PostMapping("/")
    public ResponseEntity<Void> createBook(@Valid @RequestBody CreateBookRequest request) {
        bookService.createBook(request);
        return ResponseEntity.ok().build();
    }
}
