package com.deepflow.app.domain.settings;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

@Schema(description = "Notification time update request")
public record UpdateNotificationTimeSettingRequest(
        @NotNull
        @JsonFormat(pattern = "HH:mm")
        @Schema(description = "Notification time to apply", example = "08:00", type = "string")
        LocalTime notificationTime
) {
}
