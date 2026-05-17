package com.deepflow.app.domain.settings;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Font size update request")
public record UpdateFontSizeSettingRequest(
        @NotNull
        @Min(8)
        @Max(48)
        @Schema(description = "Font size to apply", example = "18", minimum = "8", maximum = "48")
        Integer fontSize
) {
}
