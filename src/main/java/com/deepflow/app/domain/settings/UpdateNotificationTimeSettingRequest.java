package com.deepflow.app.domain.settings;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

@Schema(description = "알림 시간 변경 요청입니다.")
public record UpdateNotificationTimeSettingRequest(
        @NotNull
        @JsonFormat(pattern = "HH:mm")
        @Schema(description = "변경할 알림 시간입니다.", example = "08:00", type = "string")
        LocalTime notificationTime
) {
}
