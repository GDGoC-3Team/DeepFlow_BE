package com.deepflow.app.domain.reading;

import com.deepflow.app.common.ApiResponse;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reading")
@RequiredArgsConstructor
public class ReadingController {

    private final ReadingService readingService;
    private final FocusAnalysisService focusAnalysisService;

    // 독서 시작 처리
    @PostMapping("/start")
    public ApiResponse<ReadingSessionResponse> start(Authentication authentication, @Valid @RequestBody StartReadingRequest request) {
        return ApiResponse.ok(ReadingSessionResponse.from(readingService.startSession(authentication.getName(), request.bookId())));
    }

    // 페이지별 읽기 시간 기록
    @PostMapping("/page-time")
    public ApiResponse<Void> pageTime(Authentication authentication, @Valid @RequestBody PageTimeRequest request) {
        readingService.recordPageTime(authentication.getName(), request);
        return ApiResponse.ok(null, "Recorded");
    }

    // 독서 완료 처리
    @PostMapping("/{sessionId}/complete")
    public ApiResponse<ReadingSessionResponse> complete(Authentication authentication, @PathVariable Long sessionId) {
        return ApiResponse.ok(ReadingSessionResponse.from(readingService.completeSession(authentication.getName(), sessionId)));
    }

    // 독서 결과 조회
    @GetMapping("/result/{sessionId}")
    public ApiResponse<ReadingResultResponse> result(Authentication authentication, @PathVariable Long sessionId) {
        return ApiResponse.ok(readingService.result(authentication.getName(), sessionId));
    }

    // LLM 기반 집중도 분석 결과 조회
    @GetMapping("/focus-analysis/{sessionId}")
    public ApiResponse<FocusAnalysisResponse> focusAnalysis(Authentication authentication, @PathVariable Long sessionId) {
        return ApiResponse.ok(focusAnalysisService.analyze(authentication.getName(), sessionId));
    }

    // 독서 완료 날짜 목록 조회
    @GetMapping("/history/dates")
    public ApiResponse<List<LocalDate>> completedDates(Authentication authentication) {
        return ApiResponse.ok(readingService.completedDates(authentication.getName()));
    }

    // 월별 독서 캘린더 조회
    @GetMapping("/history/calendar")
    public ApiResponse<ReadingCalendarResponse> calendar(
            Authentication authentication,
            @RequestParam int year,
            @RequestParam int month
    ) {
        return ApiResponse.ok(readingService.calendar(authentication.getName(), year, month));
    }

    // 날짜별 독서 기록 조회
    @GetMapping("/history/{date}")
    public ApiResponse<ReadingHistoryDateResponse> historyByDate(
            Authentication authentication,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ApiResponse.ok(readingService.historyByDate(authentication.getName(), date));
    }

    // 해빗 트래커 조회
    @GetMapping("/habbit")
    public ApiResponse<HabbitResponse> habbit(Authentication authentication) {
        return ApiResponse.ok(readingService.getReadingHabbit(authentication.getName()));
    }
}
