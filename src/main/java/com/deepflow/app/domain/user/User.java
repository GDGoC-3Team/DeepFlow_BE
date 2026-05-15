package com.deepflow.app.domain.user;

import com.deepflow.app.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String firebaseUid;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nickname;

    private String fcmToken;

    @Builder
    private User(String firebaseUid, String email, String nickname, String fcmToken) {
        this.firebaseUid = firebaseUid;
        this.email = email;
        this.nickname = nickname;
        this.fcmToken = fcmToken;
    }

    public void updateProfile(String nickname, String fcmToken) {
        if (nickname != null && !nickname.isBlank()) {
            this.nickname = nickname;
        }
        if (fcmToken != null) {
            this.fcmToken = fcmToken;
        }
    }

    public void updateFromFirebase(String email, String nickname, String fcmToken) {
        this.email = email;
        updateProfile(nickname, fcmToken);
    }
}
