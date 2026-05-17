package com.deepflow.app.domain.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // Firebase 기반 사용자 생성/갱신
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

    // 사용자 정보 조회
    @Transactional(readOnly = true)
    public UserResponse getMe(String firebaseUid) {
        return UserResponse.from(findByFirebaseUid(firebaseUid));
    }

    // 사용자 정보 수정
    @Transactional
    public UserResponse updateMe(String firebaseUid, UpdateUserRequest request) {
        User user = findByFirebaseUid(firebaseUid);
        user.updateProfile(request.nickname(), request.fcmToken());
        return UserResponse.from(user);
    }

    // Firebase 기반 User 엔티티 조회 **외부 서비스용**
    @Transactional(readOnly = true)
    public User getByFirebaseUid(String firebaseUid) {
        return findByFirebaseUid(firebaseUid);
    }

    @Transactional
    public void updateFcmToken(String firebaseUid, String fcmToken) {
        findByFirebaseUid(firebaseUid).updateFcmToken(fcmToken);
    }

    @Transactional
    public void clearFcmToken(String firebaseUid) {
        findByFirebaseUid(firebaseUid).clearFcmToken();
    }

    @Transactional
    public void clearFcmTokenByUserId(Long userId) {
        userRepository.findById(userId).ifPresent(User::clearFcmToken);
    }

    // firebaseUid 기반 User 엔티티 조회 **내부 로직**
    private User findByFirebaseUid(String firebaseUid) {
        if (firebaseUid == null) {
            throw new EntityNotFoundException("Authenticated user not found");
        }
        return userRepository.findByFirebaseUid(firebaseUid)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    // 기본 닉네임 생성 로직 (email 또는 uid 기반)
    private String defaultNickname(String email, String uid) {
        if (email != null && email.contains("@")) {
            return email.substring(0, email.indexOf('@'));
        }
        return "user-" + uid.substring(0, Math.min(8, uid.length()));
    }
}
