package com.deepflow.app.domain.sentence;

import com.deepflow.app.domain.saved.SavedSentence;
import com.deepflow.app.domain.saved.SavedSentenceRepository;
import com.deepflow.app.domain.user.User;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SentenceService {

    private final SentenceRepository sentenceRepository;
    private final SavedSentenceRepository savedSentenceRepository;

    @Transactional(readOnly = true)
    public List<Sentence> randomFeed(int size) {
        return sentenceRepository.findRandom(PageRequest.of(0, Math.max(1, size)));
    }

    @Transactional
    public boolean toggleSave(User user, Long sentenceId) {
        // TODO: Save/unsave logic should stay idempotent for rapid repeated taps from the client.
        Sentence sentence = sentenceRepository.findById(sentenceId)
                .orElseThrow(() -> new EntityNotFoundException("Sentence not found"));
        return savedSentenceRepository.findByUserAndSentence(user, sentence)
                .map(saved -> {
                    savedSentenceRepository.delete(saved);
                    return false;
                })
                .orElseGet(() -> {
                    savedSentenceRepository.save(SavedSentence.of(user, sentence));
                    return true;
                });
    }
}
