package com.deepflow.app.domain.reading;

import com.deepflow.app.domain.user.User;
import com.deepflow.app.domain.user.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FocusAnalysisService {

    private final PageTimeRepository pageTimeRepository;
    private final FocusAnalysisResultRepository focusAnalysisResultRepository;
    private final UserService userService;
    private final ReadingSessionValidator readingSessionValidator;
    private final FocusAnalysisPromptBuilder focusAnalysisPromptBuilder;
    private final FocusAnalysisClient focusAnalysisClient;
    private final ObjectMapper objectMapper;

    //// =========================
    //// 집중도 분석 결과 조회
    //// =========================
    @Transactional
    public FocusAnalysisResponse analyze(String firebaseUid, Long sessionId) {

        // 1. 사용자 및 세션 소유권 검증
        User user = userService.getByFirebaseUid(firebaseUid);
        ReadingSession session = readingSessionValidator.getOwnedSession(user, sessionId);

        // 2. 기존 분석 결과 존재 시 반환
        return focusAnalysisResultRepository.findBySession(session)
                .map(this::toResponse)
                .orElseGet(() -> analyzeAndSave(session));
    }

    //// =========================
    //// 집중도 분석 생성 + 저장
    //// =========================
    private FocusAnalysisResponse analyzeAndSave(ReadingSession session) {

        // 1. 페이지 행동 데이터 조회
        List<PageTime> pageTimes = pageTimeRepository.findBySessionOrderByIdAsc(session);

        // 2. 데이터 없으면 분석 불가
        if (pageTimes.isEmpty()) {
            throw new IllegalArgumentException("Reading behavior data is required for focus analysis");
        }

        // 3. LLM 입력 데이터 생성
        FocusAnalysisInput input = focusAnalysisInput(session, pageTimes);

        // 4. LLM 분석 요청
        FocusAnalysisResponse response = focusAnalysisClient.analyze(
                session.getId(),
                focusAnalysisPromptBuilder.systemPrompt(),
                focusAnalysisPromptBuilder.userPrompt(input)
        );

        // 5. 결과 JSON 변환 후 저장
        focusAnalysisResultRepository.save(FocusAnalysisResult.of(session, toJson(response)));

        // 6. 결과 반환
        return response;
    }

    //// ================================
    //// DB 저장된 분석 결과 → Response 변환
    //// ================================
    private FocusAnalysisResponse toResponse(FocusAnalysisResult result) {
        try {
            return objectMapper.readValue(result.getResultJson(), FocusAnalysisResponse.class);
        } catch (JsonProcessingException exception) {
            throw new LlmAnalysisException("Saved focus analysis result was not valid JSON", exception);
        }
    }

    //// ================================
    //// Response → JSON 직렬화 (DB 저장용)
    //// ================================
    private String toJson(FocusAnalysisResponse response) {
        try {
            return objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException exception) {
            throw new LlmAnalysisException("Failed to serialize focus analysis result", exception);
        }
    }

    //// =========================
    //// LLM 입력 데이터 생성
    //// =========================
    private FocusAnalysisInput focusAnalysisInput(ReadingSession session, List<PageTime> pageTimes) {

        // 1. 총 독서 시간 계산
        long totalReadingSeconds = pageTimes.stream()
                .mapToLong(PageTime::getElapsedSeconds)
                .sum();

        // 2. 페이지별 체류 시간 분포 생성
        List<Long> stayTimeDistribution = pageTimes.stream()
                .map(PageTime::getElapsedSeconds)
                .toList();

        // 3. 페이지 행동 데이터 변환
        List<FocusAnalysisInput.PageBehaviorInput> pages = pageTimes.stream()
                .map(new java.util.function.Function<PageTime, FocusAnalysisInput.PageBehaviorInput>() {

                    private int previousStartOffset = 0;
                    private int order = 0;

                    @Override
                    public FocusAnalysisInput.PageBehaviorInput apply(PageTime pageTime) {
                        order++;  // 페이지 순서 증가
                        boolean movedBackward =  // 뒤로 이동 여부
                                order > 1 && pageTime.getStartOffset() < previousStartOffset;
                        previousStartOffset  // 기준 offset 갱신
                                = pageTime.getStartOffset();

                        return new FocusAnalysisInput.PageBehaviorInput(
                                order,
                                order,
                                pageTime.getStartOffset(),
                                pageTime.getEndOffset(),
                                pageTime.getCharacterCount(),
                                pageTime.getElapsedSeconds(),
                                movedBackward,
                                pageTime.isReread()
                        );
                    }
                })
                .toList();

        // 4. 최종 LLM 입력 객체 생성 후 반환
        return new FocusAnalysisInput(
                session.getId(),
                session.getDate(),
                session.getCompletedAt(),
                new FocusAnalysisInput.BookInput(
                        session.getBook().getId(),
                        session.getBook().getTitle(),
                        session.getBook().getAuthor()
                ),
                session.getCurrentOffset(),
                session.getMaxOffset(),
                totalReadingSeconds,
                stayTimeDistribution,
                pages
        );
    }
}
