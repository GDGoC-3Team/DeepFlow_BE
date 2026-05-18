package com.deepflow.app.auth;

import com.deepflow.app.common.ApiResponse;
import com.deepflow.app.domain.user.UserResponse;
import com.deepflow.app.domain.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "인증", description = "Firebase 인증 기반 로그인 API입니다.")
public class AuthController {

    private final UserService userService;

    @Operation(
            summary = "로그인 처리",
            description = "Firebase 인증 정보를 검증한 뒤 사용자 정보를 생성하거나 갱신하고 로그인 결과를 반환합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "로그인을 성공적으로 처리했습니다.",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))
            )
    })
    @PostMapping("/login")
    public ApiResponse<UserResponse> login(Authentication authentication, @Valid @RequestBody LoginRequest request) {
        FirebasePrincipal principal = (FirebasePrincipal) authentication.getPrincipal();
        log.info("Firebase login verified. uid={}", principal.uid());
        return ApiResponse.ok(UserResponse.from(userService.upsertFromFirebase(
                principal.uid(),
                principal.email(),
                request.nickname(),
                request.fcmToken()
        )));
    }
}
