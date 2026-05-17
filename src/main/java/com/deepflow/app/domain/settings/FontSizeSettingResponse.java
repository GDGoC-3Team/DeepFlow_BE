package com.deepflow.app.domain.settings;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Current font size setting")
public record FontSizeSettingResponse(
        @Schema(description = "Preferred font size", example = "18")
        int fontSize
) {

    public static FontSizeSettingResponse from(UserSetting setting) {
        return new FontSizeSettingResponse(setting.getFontSize());
    }
}
