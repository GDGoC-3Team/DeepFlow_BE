package com.deepflow.app.domain.settings;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "현재 글꼴 종류 설정입니다.")
public record FontFamilySettingResponse(
        @Schema(description = "선택된 글꼴 종류입니다.", example = "NANUM_MYEONGJO")
        FontFamily fontFamily,
        @Schema(description = "선택된 글꼴의 표시 이름입니다.", example = "나눔명조")
        String fontFamilyDisplayName
) {

    public static FontFamilySettingResponse from(UserSetting setting) {
        return new FontFamilySettingResponse(
                setting.getFontFamily(),
                setting.getFontFamily().getDisplayName()
        );
    }
}
