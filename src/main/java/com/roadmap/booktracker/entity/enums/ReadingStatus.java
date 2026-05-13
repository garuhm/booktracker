package com.roadmap.booktracker.entity.enums;

public enum ReadingStatus {
    WANT_TO_READ("Want to Read"),
    READING("Reading"),
    FINISHED("Finished");

    private final String label;

    ReadingStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}