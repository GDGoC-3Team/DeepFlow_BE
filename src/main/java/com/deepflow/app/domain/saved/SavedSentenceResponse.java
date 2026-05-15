package com.deepflow.app.domain.saved;

import com.deepflow.app.domain.sentence.Sentence;
import java.time.LocalDateTime;

public record SavedSentenceResponse(
        Long id,
        String content,
        String imageUrl,
        String bookTitle,
        String author,
        LocalDateTime savedAt
) {

    public static SavedSentenceResponse from(SavedSentence savedSentence) {
        Sentence sentence = savedSentence.getSentence();
        return new SavedSentenceResponse(
                sentence.getId(),
                sentence.getContent(),
                sentence.getImageUrl(),
                sentence.getBookTitle(),
                sentence.getAuthor(),
                savedSentence.getSavedAt()
        );
    }
}
