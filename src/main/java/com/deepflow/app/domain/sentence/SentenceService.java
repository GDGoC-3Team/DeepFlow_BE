package com.deepflow.app.domain.sentence;

import com.deepflow.app.domain.saved.SavedSentence;
import com.deepflow.app.domain.saved.SavedSentenceRepository;
import com.deepflow.app.domain.user.User;
import com.deepflow.app.domain.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SentenceService {

    private final SentenceRepository sentenceRepository;
    private final SavedSentenceRepository savedSentenceRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    public List<Sentence> randomFeed(int size) {
        return sentenceRepository.findRandom(PageRequest.of(0, Math.max(1, size)));
    }

    @Transactional(readOnly = true)
    public HomeFeedResponse getHomeFeed(LocalDate date, int size) {
        int feedSize = Math.max(1, size);
        List<Long> sentenceIds = new ArrayList<>(sentenceRepository.findAllIds());
        if (sentenceIds.isEmpty()) {
            return new HomeFeedResponse(date, List.of());
        }

        sentenceIds.sort(Comparator.comparingLong(id -> dailyOrderValueFor(date, id)));
        List<Long> selectedIds = sentenceIds.stream()
                .limit(feedSize)
                .toList();

        Map<Long, Sentence> sentenceById = new HashMap<>();
        sentenceRepository.findAllById(selectedIds)
                .forEach(sentence -> sentenceById.put(sentence.getId(), sentence));

        List<HomeSentenceResponse> items = selectedIds.stream()
                .map(sentenceById::get)
                .filter(sentence -> sentence != null)
                .map(HomeSentenceResponse::from)
                .toList();

        return new HomeFeedResponse(date, items);
    }

    @Transactional(readOnly = true)
    public Optional<HomeSentenceResponse> getPrimaryHomeFeedSentence(LocalDate date) {
        return getHomeFeed(date, 10).items().stream().findFirst();
    }

    @Transactional
    public boolean toggleSave(Long sentenceId) {
        User user = userService.getCurrentUser();
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

    private long dailyOrderValueFor(LocalDate date, Long id) {
        long seed = date.toEpochDay();
        Random random = new Random(seed ^ id);
        return random.nextLong();
    }
}
