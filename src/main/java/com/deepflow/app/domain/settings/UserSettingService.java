package com.deepflow.app.domain.settings;

import com.deepflow.app.domain.user.User;
import com.deepflow.app.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UserSettingService {

    private final UserSettingRepository userSettingRepository;
    private final UserService userService;

    @Transactional
    public UserSettingResponse get() {
        UserSetting setting = getOrCreateSetting();
        return UserSettingResponse.from(setting);
    }

    @Transactional
    public NotificationPreferenceResponse getNotificationPreference() {
        return NotificationPreferenceResponse.from(getOrCreateSetting());
    }

    @Transactional
    public NotificationPreferenceResponse updateNotificationPreference(UpdateNotificationPreferenceRequest request) {
        UserSetting setting = getOrCreateSetting();
        setting.updateNotificationEnabled(request.notificationEnabled());
        return NotificationPreferenceResponse.from(setting);
    }

    @Transactional
    public FontFamilySettingResponse getFontFamily() {
        return FontFamilySettingResponse.from(getOrCreateSetting());
    }

    @Transactional
    public FontFamilySettingResponse updateFontFamily(UpdateFontFamilySettingRequest request) {
        UserSetting setting = getOrCreateSetting();
        setting.updateFontFamily(request.fontFamily());
        return FontFamilySettingResponse.from(setting);
    }

    @Transactional
    public FontSizeSettingResponse getFontSize() {
        return FontSizeSettingResponse.from(getOrCreateSetting());
    }

    @Transactional
    public FontSizeSettingResponse updateFontSize(UpdateFontSizeSettingRequest request) {
        UserSetting setting = getOrCreateSetting();
        setting.updateFontSize(request.fontSize());
        return FontSizeSettingResponse.from(setting);
    }

    @Transactional
    public NotificationTimeSettingResponse getNotificationTime() {
        return NotificationTimeSettingResponse.from(getOrCreateSetting());
    }

    @Transactional
    public NotificationTimeSettingResponse updateNotificationTime(UpdateNotificationTimeSettingRequest request) {
        UserSetting setting = getOrCreateSetting();
        setting.updateNotificationTime(request.notificationTime());
        return NotificationTimeSettingResponse.from(setting);
    }

    @Transactional(readOnly = true)
    public NotificationTokenSettingResponse getNotificationTokenStatus() {
        User user = userService.getCurrentUser();
        return NotificationTokenSettingResponse.from(StringUtils.hasText(user.getFcmToken()));
    }

    @Transactional
    public NotificationTokenSettingResponse updateNotificationToken(UpdateNotificationTokenSettingRequest request) {
        userService.updateFcmToken(request.fcmToken().trim());
        return NotificationTokenSettingResponse.from(true);
    }

    @Transactional
    public NotificationTokenSettingResponse deleteNotificationToken() {
        userService.clearFcmToken();
        return NotificationTokenSettingResponse.from(false);
    }

    private UserSetting getOrCreateSetting() {
        User user = userService.getCurrentUser();
        return userSettingRepository.findByUser(user)
                .orElseGet(() -> userSettingRepository.save(UserSetting.defaults(user)));
    }
}
