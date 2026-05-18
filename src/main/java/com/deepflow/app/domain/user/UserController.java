package com.deepflow.app.domain.user;

import com.deepflow.app.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "사용자", description = "내 사용자 정보 조회와 수정을 처리하는 API입니다.")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "내 정보 조회",
            description = "현재 로그인한 사용자의 정보를 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "내 정보를 성공적으로 조회했습니다.",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))
            )
    })
    @GetMapping("/me")
    public ApiResponse<UserResponse> me(Authentication authentication) {
        return ApiResponse.ok(userService.getMe(authentication.getName()));
    }

    @Operation(
            summary = "내 정보 수정",
            description = "현재 로그인한 사용자의 닉네임과 알림 토큰 정보를 수정합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "내 정보를 성공적으로 수정했습니다.",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))
            )
    })
    @PatchMapping("/me")
    public ApiResponse<UserResponse> updateMe(Authentication authentication,
                                              @RequestBody UpdateUserRequest request) {
        return ApiResponse.ok(userService.updateMe(authentication.getName(), request));
    }
}
