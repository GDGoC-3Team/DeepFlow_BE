package com.deepflow.app.domain.settings;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;

public record UserSettingResponse(
        Long id,
        boolean notificationEnabled,
        @JsonFormat(pattern = "HH:mm")
        LocalTime notificationTime,
        FontFamily fontFamily,
        String fontFamilyDisplayName,
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
