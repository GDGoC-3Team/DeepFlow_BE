package com.deepflow.app.auth;

public record LoginRequest(
        String nickname,
        String fcmToken
) {
}
