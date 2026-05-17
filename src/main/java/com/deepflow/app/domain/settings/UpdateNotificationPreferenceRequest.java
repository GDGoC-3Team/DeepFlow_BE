package com.deepflow.app.domain.settings;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Notification toggle request for the home screen")
public record UpdateNotificationPreferenceRequest(
        @NotNull
        @Schema(description = "Whether push notifications are enabled", example = "true")
        Boolean notificationEnabled
) {
}
