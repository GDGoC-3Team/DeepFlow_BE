package com.deepflow.app.domain.settings;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Firebase notification token registration status")
public record NotificationTokenSettingResponse(
        @Schema(description = "Whether a Firebase Cloud Messaging token is currently registered", example = "true")
        boolean registered
) {

    public static NotificationTokenSettingResponse from(boolean registered) {
        return new NotificationTokenSettingResponse(registered);
    }
}
