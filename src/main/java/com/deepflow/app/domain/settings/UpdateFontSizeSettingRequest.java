package com.deepflow.app.domain.settings;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "글꼴 크기 변경 요청입니다.")
public record UpdateFontSizeSettingRequest(
        @NotNull
        @Min(8)
        @Max(48)
        @Schema(description = "변경할 글꼴 크기입니다.", example = "18", minimum = "8", maximum = "48")
        Integer fontSize
) {
}
