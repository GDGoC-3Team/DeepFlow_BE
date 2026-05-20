package com.deepflow.app.domain.reading;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "독서 습관 요약 정보입니다.")
public record HabbitResponse(
        @Schema(description = "연속 독서 일수입니다.", example = "7")
        @JsonProperty("streak_days")
        int streakDays,

        @Schema(description = "화면에 표시할 독서 블록 수입니다.", example = "5")
        @JsonProperty("display_blocks")
        int displayBlocks
) {
}
