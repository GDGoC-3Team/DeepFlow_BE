package com.deepflow.app.domain.settings;

import com.deepflow.app.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/settings")
@RequiredArgsConstructor
public class UserSettingController {

    private final UserSettingService userSettingService;

    @GetMapping
    public ApiResponse<UserSettingResponse> get(Authentication authentication) {
        return ApiResponse.ok(userSettingService.get(authentication.getName()));
    }

    @PatchMapping
    public ApiResponse<UserSettingResponse> update(Authentication authentication, @RequestBody UpdateUserSettingRequest request) {
        return ApiResponse.ok(userSettingService.update(authentication.getName(), request));
    }
}
