package com.deepflow.app.domain.settings;

import com.deepflow.app.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/settings")
@RequiredArgsConstructor
@Tag(name = "사용자 설정", description = "사용자 알림, 글꼴, 읽기 환경 설정을 관리하는 API입니다.")
public class UserSettingController {

    private final UserSettingService userSettingService;

    @Operation(
            summary = "사용자 설정 조회",
            description = "현재 사용자의 알림, 글꼴, 읽기 관련 설정을 한 번에 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "사용자 설정을 성공적으로 조회했습니다.",
                    content = @Content(schema = @Schema(implementation = UserSettingResponse.class))
            )
    })
    @GetMapping
    public ApiResponse<UserSettingResponse> get(Authentication authentication) {
        return ApiResponse.ok(userSettingService.get(authentication.getName()));
    }

    @Operation(
            summary = "알림 수신 여부 조회",
            description = "현재 사용자의 홈 화면 알림 수신 여부를 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "알림 수신 여부를 성공적으로 조회했습니다.",
                    content = @Content(schema = @Schema(implementation = NotificationPreferenceResponse.class))
            )
    })
    @GetMapping("/notification")
    public ApiResponse<NotificationPreferenceResponse> getNotificationPreference(Authentication authentication) {
        return ApiResponse.ok(userSettingService.getNotificationPreference(authentication.getName()));
    }

    @Operation(
            summary = "알림 수신 여부 변경",
            description = "현재 사용자의 홈 화면 알림 수신 여부를 변경합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "알림 수신 여부를 성공적으로 변경했습니다.",
                    content = @Content(schema = @Schema(implementation = NotificationPreferenceResponse.class))
            )
    })
    @PatchMapping("/notification")
    public ApiResponse<NotificationPreferenceResponse> updateNotificationPreference(
            Authentication authentication,
            @Valid @RequestBody UpdateNotificationPreferenceRequest request
    ) {
        return ApiResponse.ok(userSettingService.updateNotificationPreference(authentication.getName(), request));
    }

    @Operation(
            summary = "글꼴 종류 조회",
            description = "현재 사용자가 선택한 글꼴 종류를 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "글꼴 종류를 성공적으로 조회했습니다.",
                    content = @Content(schema = @Schema(implementation = FontFamilySettingResponse.class))
            )
    })
    @GetMapping("/font-family")
    public ApiResponse<FontFamilySettingResponse> getFontFamily(Authentication authentication) {
        return ApiResponse.ok(userSettingService.getFontFamily(authentication.getName()));
    }

    @Operation(
            summary = "글꼴 종류 변경",
            description = "현재 사용자가 사용할 글꼴 종류를 변경합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "글꼴 종류를 성공적으로 변경했습니다.",
                    content = @Content(schema = @Schema(implementation = FontFamilySettingResponse.class))
            )
    })
    @PatchMapping("/font-family")
    public ApiResponse<FontFamilySettingResponse> updateFontFamily(
            Authentication authentication,
            @Valid @RequestBody UpdateFontFamilySettingRequest request
    ) {
        return ApiResponse.ok(userSettingService.updateFontFamily(authentication.getName(), request));
    }

    @Operation(
            summary = "글꼴 크기 조회",
            description = "현재 사용자가 설정한 글꼴 크기를 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "글꼴 크기를 성공적으로 조회했습니다.",
                    content = @Content(schema = @Schema(implementation = FontSizeSettingResponse.class))
            )
    })
    @GetMapping("/font-size")
    public ApiResponse<FontSizeSettingResponse> getFontSize(Authentication authentication) {
        return ApiResponse.ok(userSettingService.getFontSize(authentication.getName()));
    }

    @Operation(
            summary = "글꼴 크기 변경",
            description = "현재 사용자가 사용할 글꼴 크기를 변경합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "글꼴 크기를 성공적으로 변경했습니다.",
                    content = @Content(schema = @Schema(implementation = FontSizeSettingResponse.class))
            )
    })
    @PatchMapping("/font-size")
    public ApiResponse<FontSizeSettingResponse> updateFontSize(
            Authentication authentication,
            @Valid @RequestBody UpdateFontSizeSettingRequest request
    ) {
        return ApiResponse.ok(userSettingService.updateFontSize(authentication.getName(), request));
    }

    @Operation(
            summary = "알림 시간 조회",
            description = "현재 사용자가 설정한 알림 시간을 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "알림 시간을 성공적으로 조회했습니다.",
                    content = @Content(schema = @Schema(implementation = NotificationTimeSettingResponse.class))
            )
    })
    @GetMapping("/notification-time")
    public ApiResponse<NotificationTimeSettingResponse> getNotificationTime(Authentication authentication) {
        return ApiResponse.ok(userSettingService.getNotificationTime(authentication.getName()));
    }

    @Operation(
            summary = "알림 시간 변경",
            description = "현재 사용자가 알림을 받을 시간을 변경합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "알림 시간을 성공적으로 변경했습니다.",
                    content = @Content(schema = @Schema(implementation = NotificationTimeSettingResponse.class))
            )
    })
    @PatchMapping("/notification-time")
    public ApiResponse<NotificationTimeSettingResponse> updateNotificationTime(
            Authentication authentication,
            @Valid @RequestBody UpdateNotificationTimeSettingRequest request
    ) {
        return ApiResponse.ok(userSettingService.updateNotificationTime(authentication.getName(), request));
    }

    @Operation(
            summary = "알림 토큰 등록 여부 조회",
            description = "현재 사용자의 푸시 알림 토큰 등록 여부를 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "알림 토큰 등록 여부를 성공적으로 조회했습니다.",
                    content = @Content(schema = @Schema(implementation = NotificationTokenSettingResponse.class))
            )
    })
    @GetMapping("/notification-token")
    public ApiResponse<NotificationTokenSettingResponse> getNotificationTokenStatus(Authentication authentication) {
        return ApiResponse.ok(userSettingService.getNotificationTokenStatus(authentication.getName()));
    }

    @Operation(
            summary = "알림 토큰 등록 또는 변경",
            description = "현재 사용자의 푸시 알림 토큰을 등록하거나 새 값으로 변경합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "알림 토큰을 성공적으로 등록하거나 변경했습니다.",
                    content = @Content(schema = @Schema(implementation = NotificationTokenSettingResponse.class))
            )
    })
    @PatchMapping("/notification-token")
    public ApiResponse<NotificationTokenSettingResponse> updateNotificationToken(
            Authentication authentication,
            @Valid @RequestBody UpdateNotificationTokenSettingRequest request
    ) {
        return ApiResponse.ok(userSettingService.updateNotificationToken(authentication.getName(), request));
    }

    @Operation(
            summary = "알림 토큰 삭제",
            description = "현재 사용자의 푸시 알림 토큰을 삭제하여 이 기기에서 더 이상 알림을 받지 않도록 합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "알림 토큰을 성공적으로 삭제했습니다.",
                    content = @Content(schema = @Schema(implementation = NotificationTokenSettingResponse.class))
            )
    })
    @DeleteMapping("/notification-token")
    public ApiResponse<NotificationTokenSettingResponse> deleteNotificationToken(Authentication authentication) {
        return ApiResponse.ok(userSettingService.deleteNotificationToken(authentication.getName()));
    }
}
