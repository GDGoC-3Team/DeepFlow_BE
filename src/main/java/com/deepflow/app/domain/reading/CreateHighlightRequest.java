package com.deepflow.app.domain.reading;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "독서 중 형광펜 표시 요청입니다.")
public record CreateHighlightRequest(
        @Schema(description = "형광펜 시작 오프셋입니다. 오늘의 글 전체 text(content) 기준 0부터 시작하는 문자 위치입니다.", example = "120")
        @Min(value = 0, message = "startOffset must be greater than or equal to 0")
        int startOffset,
        @Schema(description = "형광펜 종료 오프셋입니다. 오늘의 글 전체 text(content) 기준 0부터 시작하는 문자 위치입니다.", example = "156")
        @Min(value = 0, message = "endOffset must be greater than or equal to 0")
        int endOffset,
        @Schema(description = "형광펜 표시한 문장입니다.", example = "문장 하나가 마음에 남았다.")
        @NotBlank(message = "highlightedText is required")
        String highlightedText
) {
}
