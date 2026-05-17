package com.deepflow.app.domain.settings;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;

@Schema(description = "Aggregated user settings response")
public record UserSettingResponse(
        @Schema(description = "User setting identifier", example = "1")
        Long id,
        @Schema(description = "Whether push notifications are enabled", example = "true")
        boolean notificationEnabled,
        @JsonFormat(pattern = "HH:mm")
        @Schema(description = "Preferred notification time", example = "08:00", type = "string")
        LocalTime notificationTime,
        @Schema(description = "Selected font family", example = "NANUM_MYEONGJO")
        FontFamily fontFamily,
        @Schema(description = "Display name for the selected font family", example = "Nanum Myeongjo")
        String fontFamilyDisplayName,
        @Schema(description = "Preferred font size", example = "18")
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
