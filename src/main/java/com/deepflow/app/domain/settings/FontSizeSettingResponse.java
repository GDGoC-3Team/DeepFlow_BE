package com.deepflow.app.domain.settings;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "현재 글꼴 크기 설정입니다.")
public record FontSizeSettingResponse(
        @Schema(description = "선호하는 글꼴 크기입니다.", example = "18")
        int fontSize
) {

    public static FontSizeSettingResponse from(UserSetting setting) {
        return new FontSizeSettingResponse(setting.getFontSize());
    }
}
