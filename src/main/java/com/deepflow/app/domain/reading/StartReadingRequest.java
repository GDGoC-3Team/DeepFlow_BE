package com.deepflow.app.domain.reading;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "독서 시작 요청 정보입니다.")
public record StartReadingRequest(
        @Schema(description = "독서를 시작할 책 ID입니다.", example = "3")
        Long bookId
) {
}
