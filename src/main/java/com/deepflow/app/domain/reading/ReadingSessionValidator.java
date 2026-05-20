package com.deepflow.app.domain.reading;

import com.deepflow.app.domain.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReadingSessionValidator {

    private final ReadingRepository readingRepository;

    //// ==========================
    //// 세션 소유권 검증
    //// ==========================
    public ReadingSession getOwnedSession(User user, Long sessionId) {
        ReadingSession session = readingRepository.findById(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Reading session not found"));
        if (!session.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Reading session does not belong to user");
        }
        return session;
    }
}
