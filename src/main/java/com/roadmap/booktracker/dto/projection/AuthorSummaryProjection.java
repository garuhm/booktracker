package com.roadmap.booktracker.dto.projection;

public interface AuthorSummaryProjection {
    String getFirstName();
    String getLastName();
    Long getBookCount();
}
