package com.deepflow.app.domain.settings;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Notification toggle state for the home screen")
public record NotificationPreferenceResponse(
        @Schema(description = "Whether push notifications are enabled", example = "true")
        boolean notificationEnabled
) {

    public static NotificationPreferenceResponse from(UserSetting setting) {
        return new NotificationPreferenceResponse(setting.isNotificationEnabled());
    }
}
