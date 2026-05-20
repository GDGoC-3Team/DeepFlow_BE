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

    @Transactional
    public FocusAnalysisResponse analyze(Long sessionId) {
        User user = userService.getCurrentUser();
        ReadingSession session = readingSessionValidator.getOwnedSession(user, sessionId);

        return focusAnalysisResultRepository.findBySession(session)
                .map(this::toResponse)
                .orElseGet(() -> analyzeAndSave(session));
    }

    private FocusAnalysisResponse analyzeAndSave(ReadingSession session) {
        List<PageTime> pageTimes = pageTimeRepository.findBySessionOrderByIdAsc(session);
        if (pageTimes.isEmpty()) {
            throw new IllegalArgumentException("Reading behavior data is required for focus analysis");
        }

        FocusAnalysisInput input = focusAnalysisInput(session, pageTimes);
        FocusAnalysisResponse response = focusAnalysisClient.analyze(
                session.getId(),
                focusAnalysisPromptBuilder.systemPrompt(),
                focusAnalysisPromptBuilder.userPrompt(input)
        );

        focusAnalysisResultRepository.save(FocusAnalysisResult.of(session, toJson(response)));
        return response;
    }

    private FocusAnalysisResponse toResponse(FocusAnalysisResult result) {
        try {
            return objectMapper.readValue(result.getResultJson(), FocusAnalysisResponse.class);
        } catch (JsonProcessingException exception) {
            throw new LlmAnalysisException("Saved focus analysis result was not valid JSON", exception);
        }
    }

    private String toJson(FocusAnalysisResponse response) {
        try {
            return objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException exception) {
            throw new LlmAnalysisException("Failed to serialize focus analysis result", exception);
        }
    }

    private FocusAnalysisInput focusAnalysisInput(ReadingSession session, List<PageTime> pageTimes) {
        long totalReadingSeconds = pageTimes.stream()
                .mapToLong(PageTime::getElapsedSeconds)
                .sum();

        List<Long> stayTimeDistribution = pageTimes.stream()
                .map(PageTime::getElapsedSeconds)
                .toList();

        List<FocusAnalysisInput.PageBehaviorInput> pages = pageTimes.stream()
                .map(new java.util.function.Function<PageTime, FocusAnalysisInput.PageBehaviorInput>() {
                    private int previousStartOffset = 0;
                    private int order = 0;

                    @Override
                    public FocusAnalysisInput.PageBehaviorInput apply(PageTime pageTime) {
                        order++;
                        boolean movedBackward = order > 1 && pageTime.getStartOffset() < previousStartOffset;
                        previousStartOffset = pageTime.getStartOffset();

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
