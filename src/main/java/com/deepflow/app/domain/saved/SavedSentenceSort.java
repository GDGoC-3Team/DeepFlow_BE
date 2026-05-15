package com.deepflow.app.domain.saved;

import java.util.Set;

public enum SavedSentenceSort {
    LATEST(Set.of("latest")),
    DATE(Set.of("date")),
    CONTENT(Set.of("content"));

    private final Set<String> values;

    SavedSentenceSort(Set<String> values) {
        this.values = values;
    }

    public static SavedSentenceSort from(String value) {
        for (SavedSentenceSort sort : values()) {
            if (sort.values.contains(value)) {
                return sort;
            }
        }
        throw new IllegalArgumentException("Invalid saved sentence sort: " + value);
    }
}