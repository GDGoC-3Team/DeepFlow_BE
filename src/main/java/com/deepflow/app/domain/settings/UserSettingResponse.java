package com.deepflow.app.domain.settings;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;

@Schema(description = "사용자 설정 통합 응답입니다.")
public record UserSettingResponse(
        @Schema(description = "사용자 설정 ID입니다.", example = "1")
        Long id,
        @Schema(description = "푸시 알림 수신 여부입니다.", example = "true")
        boolean notificationEnabled,
        @JsonFormat(pattern = "HH:mm")
        @Schema(description = "설정된 알림 시간입니다.", example = "08:00", type = "string")
        LocalTime notificationTime,
        @Schema(description = "선택된 글꼴 종류입니다.", example = "NANUM_MYEONGJO")
        FontFamily fontFamily,
        @Schema(description = "선택된 글꼴의 표시 이름입니다.", example = "나눔명조")
        String fontFamilyDisplayName,
        @Schema(description = "선호하는 글꼴 크기입니다.", example = "18")
        int fontSize
) {

    public static UserSettingResponse from(UserSetting setting) {
        return new UserSettingResponse(
                setting.getId(),
                setting.isNotificationEnabled(),
                setting.getNotificationTime(),
                setting.getFontFamily(),
                setting.getFontFamily().getDisplayName(),
                setting.getFontSize()
        );
    }
}
