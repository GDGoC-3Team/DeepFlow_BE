package com.deepflow.app.domain.settings;

import com.deepflow.app.common.ApiResponse;
import com.deepflow.app.domain.user.User;
import com.deepflow.app.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/settings")
@RequiredArgsConstructor
public class UserSettingController {

    private final UserSettingRepository userSettingRepository;
    private final UserService userService;

    @GetMapping
    @Transactional
    public ApiResponse<UserSettingResponse> get(Authentication authentication) {
        User user = userService.getCurrentUser(authentication.getName());
        UserSetting setting = userSettingRepository.findByUser(user)
                .orElseGet(() -> userSettingRepository.save(UserSetting.defaults(user)));
        return ApiResponse.ok(UserSettingResponse.from(setting));
    }

    @PatchMapping
    @Transactional
    public ApiResponse<UserSettingResponse> update(Authentication authentication, @RequestBody UpdateUserSettingRequest request) {
        User user = userService.getCurrentUser(authentication.getName());
        UserSetting setting = userSettingRepository.findByUser(user)
                .orElseGet(() -> userSettingRepository.save(UserSetting.defaults(user)));
        setting.update(request);
        return ApiResponse.ok(UserSettingResponse.from(setting));
    }
}
