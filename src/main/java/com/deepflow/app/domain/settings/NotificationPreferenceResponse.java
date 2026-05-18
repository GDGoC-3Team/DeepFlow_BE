package com.deepflow.app.domain.settings;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "현재 알림 수신 설정입니다.")
public record NotificationPreferenceResponse(
        @Schema(description = "푸시 알림 수신 여부입니다.", example = "true")
        boolean notificationEnabled
) {

    public static NotificationPreferenceResponse from(UserSetting setting) {
        return new NotificationPreferenceResponse(setting.isNotificationEnabled());
    }
}
