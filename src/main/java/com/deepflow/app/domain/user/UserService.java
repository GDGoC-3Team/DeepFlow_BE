package com.deepflow.app.domain.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User upsertFromFirebase(String firebaseUid, String firebaseEmail, String nickname, String fcmToken) {
        String email = firebaseEmail == null ? "" : firebaseEmail;
        String resolvedNickname = nickname != null && !nickname.isBlank()
                ? nickname
                : defaultNickname(email, firebaseUid);
        return userRepository.findByFirebaseUid(firebaseUid)
                .map(user -> {
                    user.updateFromFirebase(email, resolvedNickname, fcmToken);
                    return user;
                })
                .orElseGet(() -> userRepository.save(User.builder()
                        .firebaseUid(firebaseUid)
                        .email(email)
                        .nickname(resolvedNickname)
                        .fcmToken(fcmToken)
                        .build()));
    }

    @Transactional(readOnly = true)
    public UserResponse getMe(String firebaseUid) {
        return UserResponse.from(findByFirebaseUid(firebaseUid));
    }

    @Transactional
    public UserResponse updateMe(String firebaseUid, UpdateUserRequest request) {
        User user = findByFirebaseUid(firebaseUid);
        user.updateProfile(request.nickname(), request.fcmToken());
        return UserResponse.from(user);
    }

    private User findByFirebaseUid(String firebaseUid) {
        if (firebaseUid == null) {
            throw new EntityNotFoundException("Authenticated user not found");
        }
        return userRepository.findByFirebaseUid(firebaseUid)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    private String defaultNickname(String email, String uid) {
        if (email != null && email.contains("@")) {
            return email.substring(0, email.indexOf('@'));
        }
        return "user-" + uid.substring(0, Math.min(8, uid.length()));
    }
}
