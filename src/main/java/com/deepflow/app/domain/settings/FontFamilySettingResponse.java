package com.deepflow.app.domain.settings;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Current font family setting")
public record FontFamilySettingResponse(
        @Schema(description = "Selected font family", example = "NANUM_MYEONGJO")
        FontFamily fontFamily,
        @Schema(description = "Display name for the selected font family", example = "Nanum Myeongjo")
        String fontFamilyDisplayName
) {

    public static FontFamilySettingResponse from(UserSetting setting) {
        return new FontFamilySettingResponse(
                setting.getFontFamily(),
                setting.getFontFamily().getDisplayName()
        );
    }
}
