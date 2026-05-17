package com.deepflow.app.domain.settings;

import com.deepflow.app.domain.user.User;
import com.deepflow.app.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserSettingService {

    private final UserSettingRepository userSettingRepository;
    private final UserService userService;

    // 사용자 설정 조회
    @Transactional
    public UserSettingResponse get(String firebaseUid) {

        UserSetting setting = getOrCreateSetting(firebaseUid);
        return UserSettingResponse.from(setting);
    }

    // 사용자 설정 수정
    @Transactional
    public UserSettingResponse update(String firebaseUid, UpdateUserSettingRequest request) {
        UserSetting setting = getOrCreateSetting(firebaseUid);
        setting.update(request);
        return UserSettingResponse.from(setting);
    }

    // 사용자 기본 설정 생성
    private UserSetting getOrCreateSetting(String firebaseUid) {
        User user = userService.getByFirebaseUid(firebaseUid);
        return userSettingRepository.findByUser(user)
                .orElseGet(() -> userSettingRepository.save(UserSetting.defaults(user)));
    }
}
