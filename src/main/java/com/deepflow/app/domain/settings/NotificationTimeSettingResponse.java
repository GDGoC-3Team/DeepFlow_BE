package com.deepflow.app.domain.settings;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;

@Schema(description = "현재 알림 시간 설정입니다.")
public record NotificationTimeSettingResponse(
        @JsonFormat(pattern = "HH:mm")
        @Schema(description = "설정된 알림 시간입니다.", example = "08:00", type = "string")
        LocalTime notificationTime
) {

    public static NotificationTimeSettingResponse from(UserSetting setting) {
        return new NotificationTimeSettingResponse(setting.getNotificationTime());
    }
}
