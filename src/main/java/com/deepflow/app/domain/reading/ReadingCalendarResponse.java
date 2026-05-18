package com.deepflow.app.domain.reading;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;

@Schema(description = "월별 독서 캘린더 정보입니다.")
public record ReadingCalendarResponse(
        @Schema(description = "조회 연도입니다.", example = "2026")
        int year,
        @Schema(description = "조회 월입니다.", example = "5")
        int month,
        @Schema(description = "오늘 날짜입니다.", example = "2026-05-18")
        LocalDate today,
        @ArraySchema(schema = @Schema(implementation = ReadingCalendarDayResponse.class))
        List<ReadingCalendarDayResponse> days
) {
}
