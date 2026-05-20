package com.deepflow.app.domain.reading;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "형광펜 표시 문장 응답입니다.")
public record HighlightResponse(
        @Schema(description = "형광펜 ID입니다.", example = "1")
        Long id,
        @Schema(description = "독서 세션 ID입니다.", example = "12")
        Long sessionId,
        @Schema(description = "형광펜 시작 오프셋입니다. 오늘의 글 전체 text(content) 기준 0부터 시작하는 문자 위치입니다.", example = "120")
        int startOffset,
        @Schema(description = "형광펜 종료 오프셋입니다. 오늘의 글 전체 text(content) 기준 0부터 시작하는 문자 위치입니다.", example = "156")
        int endOffset,
        @Schema(description = "형광펜 표시한 문장입니다.", example = "문장 하나가 마음에 남았다.")
        String highlightedText
) {

    public static HighlightResponse from(Highlight highlight) {
        return new HighlightResponse(
                highlight.getId(),
                highlight.getSession().getId(),
                highlight.getStartOffset(),
                highlight.getEndOffset(),
                highlight.getHighlightedText()
        );
    }
}
