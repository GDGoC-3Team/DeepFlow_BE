package com.deepflow.app.auth;

import com.deepflow.app.common.ApiResponse;
import com.deepflow.app.domain.user.UserResponse;
import com.deepflow.app.domain.user.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ApiResponse<UserResponse> login(@Valid @RequestBody LoginRequest request) throws Exception {
        FirebaseToken token = FirebaseAuth.getInstance().verifyIdToken(request.idToken());
        log.info("Firebase login verified. uid={}", token.getUid());
        return ApiResponse.ok(UserResponse.from(userService.upsertFromFirebase(token, request.nickname(), request.fcmToken())));
    }
}
