package com.deepflow.app.notification;

import com.deepflow.app.domain.sentence.HomeSentenceResponse;
import com.deepflow.app.domain.sentence.SentenceService;
import com.deepflow.app.domain.settings.UserSetting;
import com.deepflow.app.domain.settings.UserSettingRepository;
import com.deepflow.app.domain.user.UserService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MessagingErrorCode;
import com.google.firebase.messaging.Notification;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class NotificationScheduler {

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    private final UserSettingRepository userSettingRepository;
    private final SentenceService sentenceService;
    private final UserService userService;

    @Scheduled(cron = "0 * * * * *", zone = "Asia/Seoul")
    @Transactional
    public void sendDailySentencePush() {
        LocalDate today = LocalDate.now(KST);
        LocalTime now = LocalTime.now(KST).withSecond(0).withNano(0);
        userSettingRepository.findByNotificationEnabledTrueAndNotificationTime(now)
                .forEach(setting -> sendToUser(setting, today));
    }

    private void sendToUser(UserSetting setting, LocalDate today) {
        String token = setting.getUser().getFcmToken();
        if (!StringUtils.hasText(token) || setting.wasNotificationSentOn(today)) {
            return;
        }
        sentenceService.getPrimaryHomeFeedSentence(today).ifPresent(sentence -> {
            try {
                FirebaseMessaging.getInstance().send(buildMessage(token, sentence));
                setting.markNotificationSent(today);
            } catch (FirebaseMessagingException exception) {
                clearInvalidTokenIfNeeded(setting, exception);
            } catch (Exception ignored) {
                // Push failures should not stop notifications for other users.
            }
        });
    }

    private Message buildMessage(String token, HomeSentenceResponse sentence) {
        return Message.builder()
                .setToken(token)
                .setNotification(Notification.builder()
                        .setTitle("Today's sentence")
                        .setBody(sentence.content())
                        .build())
                .build();
    }

    private void clearInvalidTokenIfNeeded(UserSetting setting, FirebaseMessagingException exception) {
        MessagingErrorCode errorCode = exception.getMessagingErrorCode();
        if (errorCode == MessagingErrorCode.UNREGISTERED || errorCode == MessagingErrorCode.INVALID_ARGUMENT) {
            userService.clearFcmTokenByUserId(setting.getUser().getId());
        }
    }
}
