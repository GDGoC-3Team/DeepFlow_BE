package com.deepflow.app.domain.sentence;

import java.time.LocalDateTime;

public record SentenceResponse(Long id, String content, String imageUrl, String bookTitle, String author, LocalDateTime createdAt) {

    public static SentenceResponse from(Sentence sentence) {
        return new SentenceResponse(
                sentence.getId(),
                sentence.getContent(),
                sentence.getImageUrl(),
                sentence.getBookTitle(),
                sentence.getAuthor(),
                sentence.getCreatedAt()
        );
    }
}
