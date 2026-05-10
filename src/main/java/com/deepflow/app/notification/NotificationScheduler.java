package com.deepflow.app.notification;

import com.deepflow.app.domain.saved.SavedSentence;
import com.deepflow.app.domain.saved.SavedSentenceRepository;
import com.deepflow.app.domain.sentence.Sentence;
import com.deepflow.app.domain.sentence.SentenceRepository;
import com.deepflow.app.domain.settings.UserSetting;
import com.deepflow.app.domain.settings.UserSettingRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class NotificationScheduler {

    private final UserSettingRepository userSettingRepository;
    private final SavedSentenceRepository savedSentenceRepository;
    private final SentenceRepository sentenceRepository;

    @Scheduled(cron = "0 0 8 * * *", zone = "Asia/Seoul")
    @Transactional(readOnly = true)
    public void sendDailySentencePush() {
        userSettingRepository.findByNotificationEnabledTrue().forEach(this::sendToUser);
    }

    private void sendToUser(UserSetting setting) {
        String token = setting.getUser().getFcmToken();
        if (!StringUtils.hasText(token)) {
            return;
        }
        pickSentence(setting).ifPresent(sentence -> {
            try {
                Message message = Message.builder()
                        .setToken(token)
                        .setNotification(Notification.builder()
                                .setTitle("Today's sentence")
                                .setBody(sentence.getContent())
                                .build())
                        .build();
                FirebaseMessaging.getInstance().send(message);
            } catch (Exception ignored) {
                // Push failures should not stop notifications for other users.
            }
        });
    }

    private Optional<Sentence> pickSentence(UserSetting setting) {
        // Pick 1 random sentence per user from saved_sentences. Fallback to all sentences if saved list is empty.
        List<SavedSentence> saved = savedSentenceRepository.findByUserOrderBySavedAtDesc(setting.getUser());
        if (!saved.isEmpty()) {
            return Optional.of(saved.get((int) (Math.random() * saved.size())).getSentence());
        }
        return sentenceRepository.findRandom(PageRequest.of(0, 1)).stream().findFirst();
    }
}
