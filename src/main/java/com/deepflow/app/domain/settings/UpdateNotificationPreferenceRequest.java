package com.deepflow.app.domain.settings;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "알림 수신 여부 변경 요청입니다.")
public record UpdateNotificationPreferenceRequest(
        @NotNull
        @Schema(description = "푸시 알림 수신 여부입니다.", example = "true")
        Boolean notificationEnabled
) {
}
