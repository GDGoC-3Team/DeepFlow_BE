package com.deepflow.app.domain.reading;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "페이지 구간 읽기 시간 기록 요청 정보입니다.")
public record PageTimeRequest(
        @Schema(description = "독서 세션 ID입니다.", example = "1")
        Long sessionId,
        @Schema(description = "읽기 시작 위치입니다.", example = "0")
        int startOffset,
        @Schema(description = "읽기 종료 위치입니다.", example = "120")
        int endOffset,
        @Schema(description = "읽기에 소요된 시간(초)입니다.", example = "45")
        long elapsedSeconds
) {
}
