package com.deepflow.app.domain.reading;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;

@Schema(description = "날짜별 독서 기록 정보입니다.")
public record ReadingHistoryDateResponse(
        @Schema(description = "조회한 날짜입니다.", example = "2026-05-18")
        LocalDate date,
        @Schema(description = "해당 날짜에 독서를 완료했는지 여부입니다.", example = "true")
        boolean completed,
        @ArraySchema(schema = @Schema(implementation = ReadingHistoryBookResponse.class))
        List<ReadingHistoryBookResponse> books
) {
}
