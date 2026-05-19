package com.deepflow.app.domain.user;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final long FIXED_USER_ID = 1L;
    private static final String DEFAULT_FIREBASE_UID = "local-user-1";
    private static final String DEFAULT_EMAIL = "local-user-1@example.com";
    private static final String DEFAULT_NICKNAME = "Local User";

    private final UserRepository userRepository;
    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    public UserResponse getMe() {
        return UserResponse.from(getCurrentUser());
    }

    @Transactional
    public User getCurrentUser() {
        return userRepository.findById(FIXED_USER_ID)
                .orElseGet(this::createFixedUser);
    }

    @Transactional
    public void updateFcmToken(String fcmToken) {
        getCurrentUser().updateFcmToken(fcmToken);
    }

    @Transactional
    public void clearFcmToken() {
        getCurrentUser().clearFcmToken();
    }

    @Transactional
    public void clearFcmTokenByUserId(Long userId) {
        userRepository.findById(userId).ifPresent(User::clearFcmToken);
    }

    @Transactional(readOnly = true)
    public User getById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    private User createFixedUser() {
        LocalDateTime now = LocalDateTime.now();
        try {
            entityManager.createNativeQuery("""
                    insert into users (id, firebase_uid, email, nickname, fcm_token, created_at, updated_at)
                    values (:id, :firebaseUid, :email, :nickname, :fcmToken, :createdAt, :updatedAt)
                    """)
                    .setParameter("id", FIXED_USER_ID)
                    .setParameter("firebaseUid", DEFAULT_FIREBASE_UID)
                    .setParameter("email", DEFAULT_EMAIL)
                    .setParameter("nickname", DEFAULT_NICKNAME)
                    .setParameter("fcmToken", null)
                    .setParameter("createdAt", now)
                    .setParameter("updatedAt", now)
                    .executeUpdate();
        } catch (RuntimeException ignored) {
            // Another request may have created the fixed user concurrently.
        }

        return userRepository.findById(FIXED_USER_ID)
                .orElseThrow(() -> new EntityNotFoundException("Fixed user with id=1 could not be created"));
    }
}
