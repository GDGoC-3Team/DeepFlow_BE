package com.deepflow.app.domain.user;

import com.deepflow.app.common.ApiResponse;
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
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ApiResponse<UserResponse> me(Authentication authentication) {
        return ApiResponse.ok(userService.getMe(authentication.getName()));
    }

    @PatchMapping("/me")
    public ApiResponse<UserResponse> updateMe(Authentication authentication,
                                              @RequestBody UpdateUserRequest request) {
        return ApiResponse.ok(userService.updateMe(authentication.getName(), request));
    }
}
