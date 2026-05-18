package com.deepflow.app.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 요청 정보입니다.")
public record LoginRequest(
        @Schema(description = "로그인 후 사용할 닉네임입니다.", example = "독서하는고래")
        String nickname,
        @Schema(description = "기기의 FCM 알림 토큰입니다.", example = "fcm_device_token_example")
        String fcmToken
) {
}
