package com.deepflow.app.domain.settings;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "알림 토큰 등록 또는 변경 요청입니다.")
public record UpdateNotificationTokenSettingRequest(
        @NotBlank
        @Schema(description = "기기의 FCM 알림 토큰입니다.", example = "fcm_device_token_example")
        String fcmToken
) {
}
