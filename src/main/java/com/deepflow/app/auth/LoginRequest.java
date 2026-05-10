package com.deepflow.app.auth;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank String idToken,
        String nickname,
        String fcmToken
) {
}
