package com.roadmap.booktracker.entity;

import com.roadmap.booktracker.audit.AuditableEntity;
import com.roadmap.booktracker.entity.embedded.ReadingEntry;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "reading_list_entries")

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder @EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class ReadingListEntry extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false, unique = true)
    private Book book;

    @Embedded
    private ReadingEntry entry;
}
