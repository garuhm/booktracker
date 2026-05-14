package com.roadmap.booktracker.dto.projection;

public interface GenreStatsProjection {
    String getName();
    long getBookCount();
    double getAverageRating(); 
}
