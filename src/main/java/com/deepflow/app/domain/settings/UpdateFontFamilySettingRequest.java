package com.deepflow.app.domain.settings;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Font family update request")
public record UpdateFontFamilySettingRequest(
        @NotNull
        @Schema(description = "Font family to apply", example = "NANUM_MYEONGJO")
        FontFamily fontFamily
) {
}
