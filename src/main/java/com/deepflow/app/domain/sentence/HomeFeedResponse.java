package com.deepflow.app.domain.sentence;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;

@Schema(description = "Daily home feed response")
public record HomeFeedResponse(
        @Schema(description = "Feed generation date", example = "2026-05-18")
        LocalDate date,
        @ArraySchema(schema = @Schema(implementation = HomeSentenceResponse.class))
        List<HomeSentenceResponse> items
) {
}
