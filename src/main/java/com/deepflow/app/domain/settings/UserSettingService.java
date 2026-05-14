package com.deepflow.app.domain.settings;

import com.deepflow.app.domain.user.User;
import com.deepflow.app.domain.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserSettingService {

    private final UserSettingRepository userSettingRepository;
    private final UserRepository userRepository;

    @Transactional
    public UserSettingResponse get(String firebaseUid) {
        UserSetting setting = getOrCreateSetting(firebaseUid);
        return UserSettingResponse.from(setting);
    }

    @Transactional
    public UserSettingResponse update(String firebaseUid, UpdateUserSettingRequest request) {
        UserSetting setting = getOrCreateSetting(firebaseUid);
        setting.update(request);
        return UserSettingResponse.from(setting);
    }

    private UserSetting getOrCreateSetting(String firebaseUid) {
        User user = findUser(firebaseUid);
        return userSettingRepository.findByUser(user)
                .orElseGet(() -> userSettingRepository.save(UserSetting.defaults(user)));
    }

    private User findUser(String firebaseUid) {
        if (firebaseUid == null) {
            throw new EntityNotFoundException("Authenticated user not found");
        }
        return userRepository.findByFirebaseUid(firebaseUid)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
}
