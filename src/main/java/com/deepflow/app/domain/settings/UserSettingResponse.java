package com.deepflow.app.domain.settings;

import java.time.LocalTime;

public record UserSettingResponse(
        Long id,
        boolean notificationEnabled,
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
