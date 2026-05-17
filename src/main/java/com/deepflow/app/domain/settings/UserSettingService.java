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
    public UserSettingResponse get(String firebaseUid) {
        UserSetting setting = getOrCreateSetting(firebaseUid);
        return UserSettingResponse.from(setting);
    }

    @Transactional
    public NotificationPreferenceResponse getNotificationPreference(String firebaseUid) {
        return NotificationPreferenceResponse.from(getOrCreateSetting(firebaseUid));
    }

    @Transactional
    public NotificationPreferenceResponse updateNotificationPreference(
            String firebaseUid,
            UpdateNotificationPreferenceRequest request
    ) {
        UserSetting setting = getOrCreateSetting(firebaseUid);
        setting.updateNotificationEnabled(request.notificationEnabled());
        return NotificationPreferenceResponse.from(setting);
    }

    @Transactional
    public FontFamilySettingResponse getFontFamily(String firebaseUid) {
        return FontFamilySettingResponse.from(getOrCreateSetting(firebaseUid));
    }

    @Transactional
    public FontFamilySettingResponse updateFontFamily(String firebaseUid, UpdateFontFamilySettingRequest request) {
        UserSetting setting = getOrCreateSetting(firebaseUid);
        setting.updateFontFamily(request.fontFamily());
        return FontFamilySettingResponse.from(setting);
    }

    @Transactional
    public FontSizeSettingResponse getFontSize(String firebaseUid) {
        return FontSizeSettingResponse.from(getOrCreateSetting(firebaseUid));
    }

    @Transactional
    public FontSizeSettingResponse updateFontSize(String firebaseUid, UpdateFontSizeSettingRequest request) {
        UserSetting setting = getOrCreateSetting(firebaseUid);
        setting.updateFontSize(request.fontSize());
        return FontSizeSettingResponse.from(setting);
    }

    @Transactional
    public NotificationTimeSettingResponse getNotificationTime(String firebaseUid) {
        return NotificationTimeSettingResponse.from(getOrCreateSetting(firebaseUid));
    }

    @Transactional
    public NotificationTimeSettingResponse updateNotificationTime(
            String firebaseUid,
            UpdateNotificationTimeSettingRequest request
    ) {
        UserSetting setting = getOrCreateSetting(firebaseUid);
        setting.updateNotificationTime(request.notificationTime());
        return NotificationTimeSettingResponse.from(setting);
    }

    @Transactional(readOnly = true)
    public NotificationTokenSettingResponse getNotificationTokenStatus(String firebaseUid) {
        User user = userService.getByFirebaseUid(firebaseUid);
        return NotificationTokenSettingResponse.from(StringUtils.hasText(user.getFcmToken()));
    }

    @Transactional
    public NotificationTokenSettingResponse updateNotificationToken(
            String firebaseUid,
            UpdateNotificationTokenSettingRequest request
    ) {
        userService.updateFcmToken(firebaseUid, request.fcmToken().trim());
        return NotificationTokenSettingResponse.from(true);
    }

    @Transactional
    public NotificationTokenSettingResponse deleteNotificationToken(String firebaseUid) {
        userService.clearFcmToken(firebaseUid);
        return NotificationTokenSettingResponse.from(false);
    }

    private UserSetting getOrCreateSetting(String firebaseUid) {
        User user = userService.getByFirebaseUid(firebaseUid);
        return userSettingRepository.findByUser(user)
                .orElseGet(() -> userSettingRepository.save(UserSetting.defaults(user)));
    }
}
