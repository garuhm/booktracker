package com.roadmap.booktracker.repo;

import com.roadmap.booktracker.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface BookRepository extends
        JpaRepository<Book, UUID>,
        JpaSpecificationExecutor<Book> {
    Optional<Book> findByTitle(String title);

    boolean existsByTitle(String title);
}
