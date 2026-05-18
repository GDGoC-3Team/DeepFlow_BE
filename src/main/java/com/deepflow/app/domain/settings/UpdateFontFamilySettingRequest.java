package com.deepflow.app.domain.settings;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "글꼴 종류 변경 요청입니다.")
public record UpdateFontFamilySettingRequest(
        @NotNull
        @Schema(description = "변경할 글꼴 종류입니다.", example = "NANUM_MYEONGJO")
        FontFamily fontFamily
) {
}
