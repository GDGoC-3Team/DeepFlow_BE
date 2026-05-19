package com.deepflow.app.domain.sentence;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;

@Schema(description = "문장 피드 응답입니다.")
public record SentenceFeedResponse(
        @Schema(description = "피드 조회 기준 날짜입니다.", example = "2026-05-19")
        LocalDate date,
        @ArraySchema(schema = @Schema(implementation = SentenceResponse.class))
        List<SentenceResponse> items
) {
}
