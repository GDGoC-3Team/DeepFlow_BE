package com.deepflow.app.domain.saved;

public enum SavedSentenceType {
    ALL("all"),
    IMAGE("image"),
    TEXT("text");

    private final String value;

    SavedSentenceType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static SavedSentenceType from(String value) {
        for (SavedSentenceType type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid saved sentence type: " + value);
    }
}
