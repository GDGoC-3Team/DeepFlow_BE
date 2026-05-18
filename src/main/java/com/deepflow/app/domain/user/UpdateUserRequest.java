package com.deepflow.app.domain.user;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 정보 수정 요청입니다.")
public record UpdateUserRequest(
        @Schema(description = "변경할 닉네임입니다.", example = "밤에읽는여우")
        String nickname,
        @Schema(description = "변경할 FCM 알림 토큰입니다.", example = "fcm_device_token_example")
        String fcmToken
) {
}
