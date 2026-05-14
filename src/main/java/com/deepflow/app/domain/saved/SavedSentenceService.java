package com.deepflow.app.domain.saved;

import com.deepflow.app.domain.sentence.SentenceResponse;
import com.deepflow.app.domain.user.User;
import com.deepflow.app.domain.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SavedSentenceService {

    private final SavedSentenceRepository savedSentenceRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<SentenceResponse> mySentences(String firebaseUid, String sort) {
        User user = findUser(firebaseUid);
        List<SavedSentence> savedSentences = switch (sort) {
            case "date" -> savedSentenceRepository.findByUserOrderBySentenceDate(user);
            case "alphabet" -> savedSentenceRepository.findByUserOrderBySentenceContent(user);
            default -> savedSentenceRepository.findByUserOrderBySavedAtDesc(user);
        };
        return savedSentences.stream()
                .map(SavedSentence::getSentence)
                .map(SentenceResponse::from)
                .toList();
    }

    private User findUser(String firebaseUid) {
        if (firebaseUid == null) {
            throw new EntityNotFoundException("Authenticated user not found");
        }
        return userRepository.findByFirebaseUid(firebaseUid)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
}
