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
@Tag(name = "User Settings", description = "APIs for managing user notification and typography preferences")
public class UserSettingController {

    private final UserSettingService userSettingService;

    @Operation(
            summary = "Get user settings",
            description = "Returns the current user's notification, typography, and reading preference settings."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "User settings fetched successfully",
                    content = @Content(schema = @Schema(implementation = UserSettingResponse.class))
            )
    })
    @GetMapping
    public ApiResponse<UserSettingResponse> get(Authentication authentication) {
        return ApiResponse.ok(userSettingService.get(authentication.getName()));
    }

    @Operation(
            summary = "Get home notification toggle",
            description = "Returns whether the current user wants to receive push notifications from the home screen toggle."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Notification preference fetched successfully",
                    content = @Content(schema = @Schema(implementation = NotificationPreferenceResponse.class))
            )
    })
    @GetMapping("/notification")
    public ApiResponse<NotificationPreferenceResponse> getNotificationPreference(Authentication authentication) {
        return ApiResponse.ok(userSettingService.getNotificationPreference(authentication.getName()));
    }

    @Operation(
            summary = "Update home notification toggle",
            description = "Turns push notifications on or off for the current user from the home screen."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Notification preference updated successfully",
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
            summary = "Get font family setting",
            description = "Returns the current user's selected font family."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Font family fetched successfully",
                    content = @Content(schema = @Schema(implementation = FontFamilySettingResponse.class))
            )
    })
    @GetMapping("/font-family")
    public ApiResponse<FontFamilySettingResponse> getFontFamily(Authentication authentication) {
        return ApiResponse.ok(userSettingService.getFontFamily(authentication.getName()));
    }

    @Operation(
            summary = "Update font family setting",
            description = "Updates the current user's selected font family."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Font family updated successfully",
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
            summary = "Get font size setting",
            description = "Returns the current user's preferred font size."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Font size fetched successfully",
                    content = @Content(schema = @Schema(implementation = FontSizeSettingResponse.class))
            )
    })
    @GetMapping("/font-size")
    public ApiResponse<FontSizeSettingResponse> getFontSize(Authentication authentication) {
        return ApiResponse.ok(userSettingService.getFontSize(authentication.getName()));
    }

    @Operation(
            summary = "Update font size setting",
            description = "Updates the current user's preferred font size."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Font size updated successfully",
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
            summary = "Get notification time setting",
            description = "Returns the current user's preferred notification time."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Notification time fetched successfully",
                    content = @Content(schema = @Schema(implementation = NotificationTimeSettingResponse.class))
            )
    })
    @GetMapping("/notification-time")
    public ApiResponse<NotificationTimeSettingResponse> getNotificationTime(Authentication authentication) {
        return ApiResponse.ok(userSettingService.getNotificationTime(authentication.getName()));
    }

    @Operation(
            summary = "Update notification time setting",
            description = "Updates the current user's preferred notification delivery time."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Notification time updated successfully",
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
            summary = "Get Firebase notification token status",
            description = "Returns whether the current user has a registered Firebase Cloud Messaging token for push notifications."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Firebase notification token status fetched successfully",
                    content = @Content(schema = @Schema(implementation = NotificationTokenSettingResponse.class))
            )
    })
    @GetMapping("/notification-token")
    public ApiResponse<NotificationTokenSettingResponse> getNotificationTokenStatus(Authentication authentication) {
        return ApiResponse.ok(userSettingService.getNotificationTokenStatus(authentication.getName()));
    }

    @Operation(
            summary = "Register Firebase notification token",
            description = "Registers or replaces the current user's Firebase Cloud Messaging token for push notifications."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Firebase notification token updated successfully",
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
            summary = "Delete Firebase notification token",
            description = "Removes the current user's Firebase Cloud Messaging token so this device no longer receives push notifications."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Firebase notification token deleted successfully",
                    content = @Content(schema = @Schema(implementation = NotificationTokenSettingResponse.class))
            )
    })
    @DeleteMapping("/notification-token")
    public ApiResponse<NotificationTokenSettingResponse> deleteNotificationToken(Authentication authentication) {
        return ApiResponse.ok(userSettingService.deleteNotificationToken(authentication.getName()));
    }
}
