package com.deepflow.app.domain.settings;

import com.deepflow.app.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/settings")
@RequiredArgsConstructor
@Tag(name = "Settings", description = "고정 사용자 설정 API입니다.")
public class UserSettingController {

    private final UserSettingService userSettingService;

    @Operation(summary = "사용자 설정 조회", description = "고정 사용자(id=1)의 설정을 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "사용자 설정을 성공적으로 조회했습니다.",
                    content = @Content(schema = @Schema(implementation = UserSettingResponse.class))
            )
    })
    @GetMapping
    public ApiResponse<UserSettingResponse> get() {
        return ApiResponse.ok(userSettingService.get());
    }

    @Operation(summary = "알림 수신 여부 조회", description = "고정 사용자(id=1)의 알림 수신 여부를 조회합니다.")
    @GetMapping("/notification")
    public ApiResponse<NotificationPreferenceResponse> getNotificationPreference() {
        return ApiResponse.ok(userSettingService.getNotificationPreference());
    }

    @Operation(summary = "알림 수신 여부 변경", description = "고정 사용자(id=1)의 알림 수신 여부를 변경합니다.")
    @PatchMapping("/notification")
    public ApiResponse<NotificationPreferenceResponse> updateNotificationPreference(
            @Valid @RequestBody UpdateNotificationPreferenceRequest request
    ) {
        return ApiResponse.ok(userSettingService.updateNotificationPreference(request));
    }

    @Operation(summary = "글꼴 조회", description = "고정 사용자(id=1)의 글꼴 설정을 조회합니다.")
    @GetMapping("/font-family")
    public ApiResponse<FontFamilySettingResponse> getFontFamily() {
        return ApiResponse.ok(userSettingService.getFontFamily());
    }

    @Operation(summary = "글꼴 변경", description = "고정 사용자(id=1)의 글꼴 설정을 변경합니다.")
    @PatchMapping("/font-family")
    public ApiResponse<FontFamilySettingResponse> updateFontFamily(
            @Valid @RequestBody UpdateFontFamilySettingRequest request
    ) {
        return ApiResponse.ok(userSettingService.updateFontFamily(request));
    }

    @Operation(summary = "글자 크기 조회", description = "고정 사용자(id=1)의 글자 크기 설정을 조회합니다.")
    @GetMapping("/font-size")
    public ApiResponse<FontSizeSettingResponse> getFontSize() {
        return ApiResponse.ok(userSettingService.getFontSize());
    }

    @Operation(summary = "글자 크기 변경", description = "고정 사용자(id=1)의 글자 크기 설정을 변경합니다.")
    @PatchMapping("/font-size")
    public ApiResponse<FontSizeSettingResponse> updateFontSize(
            @Valid @RequestBody UpdateFontSizeSettingRequest request
    ) {
        return ApiResponse.ok(userSettingService.updateFontSize(request));
    }

    @Operation(summary = "알림 시간 조회", description = "고정 사용자(id=1)의 알림 시간을 조회합니다.")
    @GetMapping("/notification-time")
    public ApiResponse<NotificationTimeSettingResponse> getNotificationTime() {
        return ApiResponse.ok(userSettingService.getNotificationTime());
    }

    @Operation(summary = "알림 시간 변경", description = "고정 사용자(id=1)의 알림 시간을 변경합니다.")
    @PatchMapping("/notification-time")
    public ApiResponse<NotificationTimeSettingResponse> updateNotificationTime(
            @Valid @RequestBody UpdateNotificationTimeSettingRequest request
    ) {
        return ApiResponse.ok(userSettingService.updateNotificationTime(request));
    }

    @Operation(summary = "알림 토큰 등록 여부 조회", description = "고정 사용자(id=1)의 알림 토큰 등록 여부를 조회합니다.")
    @GetMapping("/notification-token")
    public ApiResponse<NotificationTokenSettingResponse> getNotificationTokenStatus() {
        return ApiResponse.ok(userSettingService.getNotificationTokenStatus());
    }

    @Operation(summary = "알림 토큰 등록 또는 변경", description = "고정 사용자(id=1)의 알림 토큰을 등록하거나 변경합니다.")
    @PatchMapping("/notification-token")
    public ApiResponse<NotificationTokenSettingResponse> updateNotificationToken(
            @Valid @RequestBody UpdateNotificationTokenSettingRequest request
    ) {
        return ApiResponse.ok(userSettingService.updateNotificationToken(request));
    }

    @Operation(summary = "알림 토큰 삭제", description = "고정 사용자(id=1)의 알림 토큰을 삭제합니다.")
    @DeleteMapping("/notification-token")
    public ApiResponse<NotificationTokenSettingResponse> deleteNotificationToken() {
        return ApiResponse.ok(userSettingService.deleteNotificationToken());
    }
}
