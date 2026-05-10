package com.deepflow.app.domain.settings;

import java.time.LocalTime;

public record UpdateUserSettingRequest(
        Boolean notificationEnabled,
        LocalTime notificationTime,
        String fontFamily,
        Integer fontSize
) {
}
