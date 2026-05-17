package com.deepflow.app.domain.settings;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;

@Schema(description = "Current notification time setting")
public record NotificationTimeSettingResponse(
        @JsonFormat(pattern = "HH:mm")
        @Schema(description = "Preferred notification time", example = "08:00", type = "string")
        LocalTime notificationTime
) {

    public static NotificationTimeSettingResponse from(UserSetting setting) {
        return new NotificationTimeSettingResponse(setting.getNotificationTime());
    }
}
