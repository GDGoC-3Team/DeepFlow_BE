package com.deepflow.app.domain.saved;

import com.deepflow.app.domain.settings.FontFamily;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "저장한 문장 정보입니다.")
public record SavedSentenceResponse(
        @Schema(description = "저장 문장 ID입니다.", example = "15")
        Long id,
        @Schema(description = "원본 문장 ID입니다. 리딩 중 저장한 문장은 null일 수 있습니다.", example = "7")
        Long sentenceId,
        @Schema(description = "리딩 세션 ID입니다. 피드에서 저장한 문장은 null일 수 있습니다.", example = "3")
        Long sessionId,
        @Schema(description = "문장 내용입니다.", example = "책은 마음을 비추는 거울이다.")
        String content,
        @Schema(description = "문장 이미지 URL입니다.", example = "https://storage.googleapis.com/deepflow-image-storage/background-image/image_1.png")
        String imageUrl,
        @Schema(description = "저장한 글꼴입니다.", example = "NANUM_MYEONGJO")
        FontFamily fontFamily,
        @Schema(description = "저장한 글자 크기입니다.", example = "18")
        Integer fontSize,
        @Schema(description = "책 제목입니다.", example = "어린 왕자")
        String bookTitle,
        @Schema(description = "저자명입니다.", example = "앙투안 드 생텍쥐페리")
        String author,
        @Schema(description = "문장을 저장한 시각입니다.", example = "2026-05-18T10:15:00")
        LocalDateTime savedAt
) {

    public static SavedSentenceResponse from(SavedSentence savedSentence) {
        return new SavedSentenceResponse(
                savedSentence.getId(),
                savedSentence.getSentence() == null ? null : savedSentence.getSentence().getId(),
                savedSentence.getSession() == null ? null : savedSentence.getSession().getId(),
                savedSentence.getDisplayContent(),
                savedSentence.getDisplayImageUrl(),
                savedSentence.getFontFamily(),
                savedSentence.getFontSize(),
                savedSentence.getDisplayBookTitle(),
                savedSentence.getDisplayAuthor(),
                savedSentence.getSavedAt()
        );
    }
}
