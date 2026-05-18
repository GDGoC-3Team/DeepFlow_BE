package com.deepflow.app.domain.user;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 정보입니다.")
public record UserResponse(
        @Schema(description = "사용자 ID입니다.", example = "1")
        Long id,
        @Schema(description = "사용자 이메일입니다.", example = "reader@example.com")
        String email,
        @Schema(description = "사용자 닉네임입니다.", example = "독서하는고래")
        String nickname
) {

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getNickname()
        );
    }
}
