package com.deepflow.app.domain.user;

import java.time.LocalDateTime;

public record UserResponse(Long id, String firebaseUid, String email, String nickname, String fcmToken, LocalDateTime createdAt) {

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirebaseUid(),
                user.getEmail(),
                user.getNickname(),
                user.getFcmToken(),
                user.getCreatedAt()
        );
    }
}
