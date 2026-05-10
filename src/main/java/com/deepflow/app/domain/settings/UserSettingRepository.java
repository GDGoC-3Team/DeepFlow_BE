package com.deepflow.app.domain.settings;

import com.deepflow.app.domain.user.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSettingRepository extends JpaRepository<UserSetting, Long> {

    Optional<UserSetting> findByUser(User user);

    List<UserSetting> findByNotificationEnabledTrue();
}
