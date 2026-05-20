package com.deepflow.app.domain.settings;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "알림 토큰 등록 상태입니다.")
public record NotificationTokenSettingResponse(
        @Schema(description = "푸시 알림 토큰이 등록되어 있는지 여부입니다.", example = "true")
        boolean registered
) {

    public static NotificationTokenSettingResponse from(boolean registered) {
        return new NotificationTokenSettingResponse(registered);
    }
}
