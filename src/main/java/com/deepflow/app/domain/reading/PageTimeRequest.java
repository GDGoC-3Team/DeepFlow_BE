package com.deepflow.app.domain.reading;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "페이지 읽기 시간 기록 요청 정보입니다.")
public record PageTimeRequest(
        @Schema(description = "독서 세션 ID입니다.", example = "1")
        @NotNull(message = "sessionId is required")
        Long sessionId,
        @Schema(description = "읽은 페이지 번호입니다. 1부터 시작합니다.", example = "3")
        @Min(value = 1, message = "pageNumber must be greater than or equal to 1")
        int pageNumber,
        @Schema(description = "해당 페이지를 읽는 데 걸린 시간(초)입니다.", example = "45")
        @Min(value = 0, message = "elapsedSeconds must be greater than or equal to 0")
        long elapsedSeconds
) {
}
