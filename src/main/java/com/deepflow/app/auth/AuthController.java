package com.deepflow.app.auth;

import com.deepflow.app.common.ApiResponse;
import com.deepflow.app.domain.user.UserResponse;
import com.deepflow.app.domain.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "고정 사용자 로그인 API입니다.")
public class AuthController {

    private final UserService userService;

    @Operation(
            summary = "로그인 처리",
            description = "인증 없이 고정 사용자(id=1) 정보를 반환합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "고정 사용자 정보를 반환했습니다.",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))
            )
    })
    @PostMapping("/login")
    public ApiResponse<UserResponse> login(@RequestBody(required = false) LoginRequest request) {
        return ApiResponse.ok(userService.getMe());
    }
}
