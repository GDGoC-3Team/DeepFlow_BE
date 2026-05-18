package com.deepflow.app.domain.reading;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "페이지별 집중도 분석 결과입니다.")
public record FocusAnalysisResponse(
        @ArraySchema(schema = @Schema(implementation = PageFocusResult.class))
        List<PageFocusResult> pageFocusResults
) {

    @Schema(description = "페이지별 집중도 분석 정보입니다.")
    public record PageFocusResult(
            @Schema(description = "페이지 번호입니다.", example = "12")
            int pageIndex,
            @Schema(description = "집중도 점수입니다.", example = "82")
            int focusScore,
            @Schema(description = "집중도 분석 설명입니다.", example = "중간 구간에서 읽기 속도가 안정적으로 유지되었습니다.")
            String explanation
    ) {
    }
}
