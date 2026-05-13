package com.deepflow.app.auth;

import com.deepflow.app.common.ApiResponse;
import com.deepflow.app.domain.user.UserResponse;
import com.deepflow.app.domain.user.UserService;
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
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ApiResponse<UserResponse> login(Authentication authentication, @Valid @RequestBody LoginRequest request) {
        FirebasePrincipal principal = (FirebasePrincipal) authentication.getPrincipal();
        log.info("Firebase login verified. uid={}", principal.uid());
        return ApiResponse.ok(UserResponse.from(userService.upsertFromFirebase(principal, request.nickname(), request.fcmToken())));
    }
}
