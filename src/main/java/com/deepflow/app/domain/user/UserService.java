package com.deepflow.app.domain.user;

import com.deepflow.app.auth.FirebasePrincipal;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User upsertFromFirebase(FirebasePrincipal principal, String nickname, String fcmToken) {
        String uid = principal.uid();
        String email = principal.email() == null ? "" : principal.email();
        String resolvedNickname = nickname != null && !nickname.isBlank()
                ? nickname
                : defaultNickname(email, uid);
        return userRepository.findByFirebaseUid(uid)
                .map(user -> {
                    user.updateFromFirebase(email, resolvedNickname, fcmToken);
                    return user;
                })
                .orElseGet(() -> userRepository.save(User.builder()
                        .firebaseUid(uid)
                        .email(email)
                        .nickname(resolvedNickname)
                        .fcmToken(fcmToken)
                        .build()));
    }

    @Transactional(readOnly = true)
    public User getCurrentUser(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            throw new EntityNotFoundException("Authenticated user not found");
        }
        return userRepository.findByFirebaseUid(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Transactional
    public User updateCurrentUser(Authentication authentication, UpdateUserRequest request) {
        User user = getCurrentUser(authentication);
        user.updateProfile(request.nickname(), request.fcmToken());
        return user;
    }

    private String defaultNickname(String email, String uid) {
        if (email != null && email.contains("@")) {
            return email.substring(0, email.indexOf('@'));
        }
        return "user-" + uid.substring(0, Math.min(8, uid.length()));
    }
}
