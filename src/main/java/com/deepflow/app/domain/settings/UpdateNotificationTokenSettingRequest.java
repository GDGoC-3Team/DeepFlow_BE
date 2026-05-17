package com.deepflow.app.domain.settings;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Firebase notification token registration request")
public record UpdateNotificationTokenSettingRequest(
        @NotBlank
        @Schema(description = "Firebase Cloud Messaging device token", example = "fcm_device_token_example")
        String fcmToken
) {
}
