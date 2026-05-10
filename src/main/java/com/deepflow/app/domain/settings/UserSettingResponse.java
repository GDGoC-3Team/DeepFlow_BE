package com.deepflow.app.domain.settings;

import java.time.LocalTime;

public record UserSettingResponse(
        Long id,
        boolean notificationEnabled,
        LocalTime notificationTime,
        String fontFamily,
        int fontSize
) {

    public static UserSettingResponse from(UserSetting setting) {
        return new UserSettingResponse(
                setting.getId(),
                setting.isNotificationEnabled(),
                setting.getNotificationTime(),
                setting.getFontFamily(),
                setting.getFontSize()
        );
    }
}
