package com.deepflow.app.domain.settings;

import java.time.LocalTime;

public record UpdateUserSettingRequest(
        Boolean notificationEnabled,
        LocalTime notificationTime,
        FontFamily fontFamily,
        Integer fontSize
) {
}
