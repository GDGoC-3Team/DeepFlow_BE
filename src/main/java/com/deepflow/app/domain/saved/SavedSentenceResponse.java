package com.deepflow.app.domain.saved;

import com.deepflow.app.domain.sentence.Sentence;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "저장한 문장 정보입니다.")
public record SavedSentenceResponse(
        @Schema(description = "문장 ID입니다.", example = "15")
        Long id,
        @Schema(description = "문장 내용입니다.", example = "책은 마음을 비추는 거울이다.")
        String content,
        @Schema(description = "문장 이미지 URL입니다.", example = "https://storage.googleapis.com/deepflow/sentences/example.jpg")
        String imageUrl,
        @Schema(description = "책 제목입니다.", example = "어린 왕자")
        String bookTitle,
        @Schema(description = "저자명입니다.", example = "앙투안 드 생텍쥐페리")
        String author,
        @Schema(description = "문장을 저장한 시각입니다.", example = "2026-05-18T10:15:00")
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
