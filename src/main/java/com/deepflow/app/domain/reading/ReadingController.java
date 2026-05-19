package com.deepflow.app.domain.reading;

import com.deepflow.app.common.ApiResponse;
import com.deepflow.app.domain.saved.CreateSavedSentenceRequest;
import com.deepflow.app.domain.saved.SavedSentenceResponse;
import com.deepflow.app.domain.saved.SavedSentenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    private final SavedSentenceService savedSentenceService;

    @Operation(
            summary = "오늘의 글 조회",
            description = "고정 사용자(id=1) 기준 오늘 읽을 글을 추천합니다.",
            tags = {"독서 세션"}
    )
    @GetMapping("/today")
    public ApiResponse<TodayReadingResponse> todayReading() {
        return ApiResponse.ok(readingService.todayReading());
    }

    @Operation(
            summary = "독서 시작",
            description = "선택한 책으로 독서 세션을 시작합니다.",
            tags = {"독서 세션"}
    )
    @PostMapping("/start")
    public ApiResponse<ReadingSessionResponse> start(@Valid @RequestBody StartReadingRequest request) {
        return ApiResponse.ok(ReadingSessionResponse.from(readingService.startSession(request.bookId())));
    }

    @Operation(
            summary = "페이지 구간 읽기 시간 기록",
            description = "독서 중 페이지 구간 읽기 시간을 기록합니다.",
            tags = {"독서 중 형광펜, 문장 저장"}
    )
    @PostMapping("/page-time")
    public ApiResponse<Void> pageTime(@Valid @RequestBody PageTimeRequest request) {
        readingService.recordPageTime(request);
        return ApiResponse.ok(null, "Recorded");
    }

    @Operation(
            summary = "독서 완료",
            description = "진행 중인 독서 세션을 완료 처리합니다.",
            tags = {"독서 세션"}
    )
    @PostMapping("/{sessionId}/complete")
    public ApiResponse<ReadingSessionResponse> complete(@PathVariable Long sessionId) {
        return ApiResponse.ok(ReadingSessionResponse.from(readingService.completeSession(sessionId)));
    }

    @Operation(
            summary = "형광펜 생성",
            description = "독서 중 선택한 문장을 형광펜으로 저장합니다.",
            tags = {"독서 중 형광펜, 문장 저장"}
    )
    @PostMapping("/{sessionId}/highlights")
    public ApiResponse<HighlightResponse> createHighlight(
            @PathVariable Long sessionId,
            @Valid @RequestBody CreateHighlightRequest request
    ) {
        return ApiResponse.ok(readingService.createHighlight(sessionId, request));
    }

    @Operation(
            summary = "문장 저장",
            description = "독서 중 선택한 문장을 저장 문장으로 생성합니다.",
            tags = {"독서 중 형광펜, 문장 저장"}
    )
    @PostMapping("/{sessionId}/saved-sentences")
    public ApiResponse<SavedSentenceResponse> createSavedSentence(
            @PathVariable Long sessionId,
            @Valid @RequestBody CreateSavedSentenceRequest request
    ) {
        return ApiResponse.ok(savedSentenceService.createFromReading(sessionId, request));
    }

    @Operation(
            summary = "형광펜 삭제",
            description = "저장한 형광펜을 삭제합니다.",
            tags = {"독서 중 형광펜, 문장 저장"}
    )
    @DeleteMapping("/{sessionId}/highlights/{highlightId}")
    public ApiResponse<Void> deleteHighlight(
            @PathVariable Long sessionId,
            @PathVariable Long highlightId
    ) {
        readingService.deleteHighlight(sessionId, highlightId);
        return ApiResponse.ok(null, "Deleted");
    }

    @Operation(
            summary = "형광펜 목록 조회",
            description = "독서 세션의 형광펜 목록을 조회합니다.",
            tags = {"독서 중 형광펜, 문장 저장"}
    )
    @GetMapping("/{sessionId}/highlights")
    public ApiResponse<List<HighlightResponse>> highlights(@PathVariable Long sessionId) {
        return ApiResponse.ok(readingService.getHighlights(sessionId));
    }

    @Operation(
            summary = "독서 결과 조회",
            description = "독서 세션의 결과를 조회합니다.",
            tags = {"독서 기록 조회"}
    )
    @GetMapping("/result/{sessionId}")
    public ApiResponse<ReadingResultResponse> result(@PathVariable Long sessionId) {
        return ApiResponse.ok(readingService.result(sessionId));
    }

    @Operation(
            summary = "집중도 분석 조회",
            description = "독서 세션의 집중도 분석 결과를 조회합니다.",
            tags = {"독서 기록 조회"}
    )
    @GetMapping("/focus-analysis/{sessionId}")
    public ApiResponse<FocusAnalysisResponse> focusAnalysis(@PathVariable Long sessionId) {
        return ApiResponse.ok(focusAnalysisService.analyze(sessionId));
    }

    @Operation(
            summary = "완독 날짜 목록 조회",
            description = "고정 사용자(id=1)의 완독 날짜 목록을 조회합니다.",
            tags = {"독서 기록 조회"}
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "완독 날짜 목록을 성공적으로 조회했습니다.",
                    content = @Content(array = @ArraySchema(schema = @Schema(type = "string", format = "date", example = "2026-05-18")))
            )
    })
    @GetMapping("/history/dates")
    public ApiResponse<List<LocalDate>> completedDates() {
        return ApiResponse.ok(readingService.completedDates());
    }

    @Operation(
            summary = "월간 독서 캘린더 조회",
            description = "지정한 연월의 독서 완료 여부를 조회합니다.",
            tags = {"독서 기록 조회"}
    )
    @GetMapping("/history/calendar")
    public ApiResponse<ReadingCalendarResponse> calendar(
            @Parameter(description = "조회할 연도입니다.", example = "2026")
            @RequestParam int year,
            @Parameter(description = "조회할 월입니다.", example = "5")
            @RequestParam int month
    ) {
        return ApiResponse.ok(readingService.calendar(year, month));
    }

    @Operation(
            summary = "독서 기록 조회",
            description = "지정한 날짜의 독서 기록을 조회합니다.",
            tags = {"독서 기록 조회"}
    )
    @GetMapping("/history/{date}")
    public ApiResponse<ReadingHistoryDateResponse> historyByDate(
            @Parameter(description = "조회할 날짜입니다.", example = "2026-05-18")
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ApiResponse.ok(readingService.historyByDate(date));
    }

    @Operation(
            summary = "독서 습관 조회",
            description = "고정 사용자(id=1)의 독서 습관 지표를 조회합니다.",
            tags = {"독서 기록 조회"}
    )
    @GetMapping("/habbit")
    public ApiResponse<HabbitResponse> habbit() {
        return ApiResponse.ok(readingService.getReadingHabbit());
    }
}
