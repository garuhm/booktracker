package com.roadmap.booktracker.repo;

import com.roadmap.booktracker.entity.ReadingListEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReadingListEntryRepository extends JpaRepository<ReadingListEntry, UUID> {
}
