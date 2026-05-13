package com.roadmap.booktracker.entity;

import com.roadmap.booktracker.audit.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "books")

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder @EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Book extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private int pages;

    @Column(nullable = false)
    private int publishedYear;

    @Builder.Default
    private double averageRating = 0.0;

    @Builder.Default
    @ManyToMany(mappedBy = "books")
    private Set<Author> authors = Set.of();

    @Builder.Default
    @ManyToMany(mappedBy = "books")
    private Set<Genre> genres = Set.of();

    @Builder.Default
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private Set<Review> reviews = Set.of();
}
