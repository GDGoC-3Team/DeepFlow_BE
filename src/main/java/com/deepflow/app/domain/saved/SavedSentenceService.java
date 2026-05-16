package com.deepflow.app.domain.saved;

import com.deepflow.app.domain.user.User;
import com.deepflow.app.domain.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SavedSentenceService {

    private final SavedSentenceRepository savedSentenceRepository;
    private final UserService userService;


    // 저장한 문장 목록 조회
    @Transactional(readOnly = true)
    public List<SavedSentenceResponse> mySentences(String firebaseUid, String type, String sort) {

        // 1. 유저 조회
        User user = userService.getByFirebaseUid(firebaseUid);

        // 2. sort 기준 정렬
        SavedSentenceSort savedSentenceSort = SavedSentenceSort.from(sort);
        SavedSentenceType savedSentenceType = SavedSentenceType.from(type);
        List<SavedSentence> savedSentences = switch (savedSentenceSort) {
            // 날짜순
            case DATE -> savedSentenceRepository.findByUserAndTypeOrderBySentenceDate(user, savedSentenceType.value());
            // 가나다순
            case CONTENT -> savedSentenceRepository.findByUserAndTypeOrderBySentenceContent(user, savedSentenceType.value());
            // 최신순
            case LATEST -> savedSentenceRepository.findByUserAndTypeOrderBySavedAtDesc(user, savedSentenceType.value());
        };

        // 3. type(image/text) 필터링하여 반환
        return savedSentences.stream()
                .filter(savedSentence -> matchesType(savedSentence, savedSentenceType))
                .map(SavedSentenceResponse::from)
                .toList();
    }


    // 저장된 문장 삭제 (문장 자체 삭제 X, 유저의 저장 기록 여부만 삭제)
    @Transactional
    public void delete(String firebaseUid, Long sentenceId) {

        // 1. 유저 조회
        User user = userService.getByFirebaseUid(firebaseUid);

        // 2. 해당 유저가 저장한 문장 조회
        SavedSentence savedSentence = savedSentenceRepository.findByUserAndSentenceId(user, sentenceId)
                .orElseThrow(() -> new EntityNotFoundException("Saved sentence not found"));

        // 3. 문장 삭제 (저장한 문장 목록에서만 삭제. 원본 sentence 유지)
        savedSentenceRepository.delete(savedSentence);
    }


    // type(image/text/all) 기준 필터링 로직
    private boolean matchesType(SavedSentence savedSentence, SavedSentenceType type) {

        // 이미지 존재 여부 판단
        String imageUrl = savedSentence.getSentence().getImageUrl();
        boolean hasImage = imageUrl != null && !imageUrl.isBlank();

        return switch (type) {
            case IMAGE -> hasImage; // 이미지 문장만 필터링
            case TEXT -> !hasImage; // 텍스트 문장만 필터링
            case ALL -> true;
        };
    }
}
