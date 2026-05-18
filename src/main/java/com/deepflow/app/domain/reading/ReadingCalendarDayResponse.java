package com.deepflow.app.domain.reading;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "독서 캘린더의 하루 정보입니다.")
public record ReadingCalendarDayResponse(
        @Schema(description = "날짜입니다.", example = "2026-05-18")
        LocalDate date,
        @Schema(description = "해당 날짜에 독서를 완료했는지 여부입니다.", example = "true")
        boolean completed,
        @Schema(description = "오늘 날짜인지 여부입니다.", example = "false")
        boolean today
) {
}
