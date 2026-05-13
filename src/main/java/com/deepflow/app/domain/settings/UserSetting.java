package com.deepflow.app.domain.settings;

import com.deepflow.app.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "user_settings")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    private boolean notificationEnabled;

    private LocalTime notificationTime;

    @Column(columnDefinition = "ENUM('NANUM_MYEONGJO','KOPUB_BATANG','NOTO_SANS')")
    @Convert(converter = FontFamilyConverter.class)
    private FontFamily fontFamily;

    private int fontSize;

    public static UserSetting defaults(User user) {
        UserSetting setting = new UserSetting();
        setting.user = user;
        setting.notificationEnabled = true;
        setting.notificationTime = LocalTime.of(8, 0);
        setting.fontFamily = FontFamily.NANUM_MYEONGJO;
        setting.fontSize = 18;
        return setting;
    }

    public void update(UpdateUserSettingRequest request) {
        if (request.notificationEnabled() != null) {
            this.notificationEnabled = request.notificationEnabled();
        }
        if (request.notificationTime() != null) {
            this.notificationTime = request.notificationTime();
        }
        if (request.fontFamily() != null) {
            this.fontFamily = request.fontFamily();
        }
        if (request.fontSize() != null) {
            this.fontSize = request.fontSize();
        }
    }
}
