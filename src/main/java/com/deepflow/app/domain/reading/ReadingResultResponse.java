package com.deepflow.app.domain.reading;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "독서 결과 정보입니다.")
public record ReadingResultResponse(
        @Schema(description = "독서 세션 ID입니다.", example = "1")
        Long sessionId,
        @Schema(description = "현재 읽은 마지막 위치입니다.", example = "320")
        int currentOffset,
        @Schema(description = "목표 또는 최대 읽기 위치입니다.", example = "540")
        int maxOffset,
        @Schema(description = "전체 진행률입니다.", example = "59.26")
        double progressPercent,
        @Schema(description = "문자당 평균 읽기 시간입니다.", example = "0.42")
        double averageSecondsPerCharacter,
        @ArraySchema(schema = @Schema(implementation = SegmentBreakdown.class))
        List<SegmentBreakdown> segments
) {

    @Schema(description = "독서 구간별 세부 결과입니다.")
    public record SegmentBreakdown(
            @Schema(description = "구간 시작 위치입니다.", example = "0")
            int startOffset,
            @Schema(description = "구간 종료 위치입니다.", example = "120")
            int endOffset,
            @Schema(description = "해당 구간을 읽는 데 걸린 시간(초)입니다.", example = "45")
            long elapsedSeconds,
            @Schema(description = "해당 구간의 문자 수입니다.", example = "120")
            int characterCount,
            @Schema(description = "문자당 소요 시간입니다.", example = "0.38")
            double secondsPerCharacter,
            @Schema(description = "이상치 여부입니다.", example = "false")
            boolean outlier,
            @Schema(description = "재독 여부입니다.", example = "true")
            boolean reread
    ) {
    }
}
