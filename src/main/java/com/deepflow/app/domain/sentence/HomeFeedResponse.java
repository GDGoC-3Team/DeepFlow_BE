package com.deepflow.app.domain.sentence;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;

@Schema(description = "홈 문장 피드 응답입니다.")
public record HomeFeedResponse(
        @Schema(description = "피드 기준 날짜입니다.", example = "2026-05-18")
        LocalDate date,
        @ArraySchema(schema = @Schema(implementation = HomeSentenceResponse.class))
        List<HomeSentenceResponse> items
) {
}
