package com.deepflow.app.domain.saved;

import com.deepflow.app.domain.reading.ReadingSession;
import com.deepflow.app.domain.reading.ReadingSessionValidator;
import com.deepflow.app.domain.user.User;
import com.deepflow.app.domain.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SavedSentenceService {

    private final SavedSentenceRepository savedSentenceRepository;
    private final UserService userService;
    private final ReadingSessionValidator readingSessionValidator;

    @Transactional
    public SavedSentenceResponse createFromReading(Long sessionId, CreateSavedSentenceRequest request) {
        User user = userService.getCurrentUser();
        ReadingSession session = readingSessionValidator.getOwnedSession(user, sessionId);

        validateRange(request);

        SavedSentence savedSentence = savedSentenceRepository.save(SavedSentence.fromReading(
                user,
                session,
                request.selectedText().trim(),
                normalizeImageUrl(request.imageUrl()),
                request.fontFamily(),
                request.fontSize()
        ));
        return SavedSentenceResponse.from(savedSentence);
    }

    @Transactional(readOnly = true)
    public List<SavedSentenceResponse> mySentences(String type, String sort) {
        User user = userService.getCurrentUser();
        SavedSentenceType savedSentenceType = SavedSentenceType.from(type);
        SavedSentenceSort savedSentenceSort = SavedSentenceSort.from(sort);

        return savedSentenceRepository.findByUserOrderBySavedAtDesc(user).stream()
                .filter(savedSentence -> matchesType(savedSentence, savedSentenceType))
                .sorted(comparator(savedSentenceSort))
                .map(SavedSentenceResponse::from)
                .toList();
    }

    @Transactional
    public void delete(Long savedSentenceId) {
        User user = userService.getCurrentUser();
        SavedSentence savedSentence = savedSentenceRepository.findByIdAndUser(savedSentenceId, user)
                .orElseThrow(() -> new EntityNotFoundException("Saved sentence not found"));
        savedSentenceRepository.delete(savedSentence);
    }

    private void validateRange(CreateSavedSentenceRequest request) {
        if (request.endOffset() < request.startOffset()) {
            throw new IllegalArgumentException("endOffset must be greater than or equal to startOffset");
        }
    }

    private String normalizeImageUrl(String imageUrl) {
        if (imageUrl == null) {
            return null;
        }
        String trimmed = imageUrl.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private Comparator<SavedSentence> comparator(SavedSentenceSort sort) {
        return switch (sort) {
            case LATEST -> Comparator.comparing(SavedSentence::getSavedAt).reversed();
            case DATE -> Comparator.comparing(SavedSentence::getSavedAt);
            case CONTENT -> Comparator.comparing(
                    savedSentence -> savedSentence.getDisplayContent(),
                    Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)
            );
        };
    }

    private boolean matchesType(SavedSentence savedSentence, SavedSentenceType type) {
        boolean hasImage = savedSentence.getDisplayImageUrl() != null && !savedSentence.getDisplayImageUrl().isBlank();
        return switch (type) {
            case IMAGE -> hasImage;
            case TEXT -> !hasImage;
            case ALL -> true;
        };
    }
}
