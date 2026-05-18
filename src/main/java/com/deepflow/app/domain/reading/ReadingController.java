package com.deepflow.app.domain.reading;

import com.deepflow.app.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "독서", description = "독서 세션 진행, 기록, 통계 조회를 처리하는 API입니다.")
public class ReadingController {

    private final ReadingService readingService;
    private final FocusAnalysisService focusAnalysisService;

    @Operation(
            summary = "독서 시작",
            description = "선택한 책으로 새로운 독서 세션을 시작합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "독서 세션을 성공적으로 시작했습니다.",
                    content = @Content(schema = @Schema(implementation = ReadingSessionResponse.class))
            )
    })
    @PostMapping("/start")
    public ApiResponse<ReadingSessionResponse> start(Authentication authentication, @Valid @RequestBody StartReadingRequest request) {
        return ApiResponse.ok(ReadingSessionResponse.from(readingService.startSession(authentication.getName(), request.bookId())));
    }

    @Operation(
            summary = "페이지 구간 읽기 시간 기록",
            description = "독서 세션에서 특정 페이지 구간을 읽는 데 걸린 시간을 기록합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "페이지 구간 읽기 시간을 성공적으로 기록했습니다."
            )
    })
    @PostMapping("/page-time")
    public ApiResponse<Void> pageTime(Authentication authentication, @Valid @RequestBody PageTimeRequest request) {
        readingService.recordPageTime(authentication.getName(), request);
        return ApiResponse.ok(null, "Recorded");
    }

    @Operation(
            summary = "독서 완료",
            description = "진행 중인 독서 세션을 완료 처리합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "독서 세션을 성공적으로 완료했습니다.",
                    content = @Content(schema = @Schema(implementation = ReadingSessionResponse.class))
            )
    })
    @PostMapping("/{sessionId}/complete")
    public ApiResponse<ReadingSessionResponse> complete(Authentication authentication, @PathVariable Long sessionId) {
        return ApiResponse.ok(ReadingSessionResponse.from(readingService.completeSession(authentication.getName(), sessionId)));
    }

    @Operation(
            summary = "독서 결과 조회",
            description = "독서 세션의 진행률과 구간별 읽기 결과를 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "독서 결과를 성공적으로 조회했습니다.",
                    content = @Content(schema = @Schema(implementation = ReadingResultResponse.class))
            )
    })
    @GetMapping("/result/{sessionId}")
    public ApiResponse<ReadingResultResponse> result(Authentication authentication, @PathVariable Long sessionId) {
        return ApiResponse.ok(readingService.result(authentication.getName(), sessionId));
    }

    @Operation(
            summary = "집중도 분석 결과 조회",
            description = "독서 세션의 페이지별 집중도 분석 결과를 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "집중도 분석 결과를 성공적으로 조회했습니다.",
                    content = @Content(schema = @Schema(implementation = FocusAnalysisResponse.class))
            )
    })
    @GetMapping("/focus-analysis/{sessionId}")
    public ApiResponse<FocusAnalysisResponse> focusAnalysis(Authentication authentication, @PathVariable Long sessionId) {
        return ApiResponse.ok(focusAnalysisService.analyze(authentication.getName(), sessionId));
    }

    @Operation(
            summary = "완독 날짜 목록 조회",
            description = "현재 사용자가 독서를 완료한 날짜 목록을 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "완독 날짜 목록을 성공적으로 조회했습니다.",
                    content = @Content(array = @ArraySchema(schema = @Schema(type = "string", format = "date", example = "2026-05-18")))
            )
    })
    @GetMapping("/history/dates")
    public ApiResponse<List<LocalDate>> completedDates(Authentication authentication) {
        return ApiResponse.ok(readingService.completedDates(authentication.getName()));
    }

    @Operation(
            summary = "월별 독서 캘린더 조회",
            description = "지정한 연도와 월을 기준으로 독서 완료 여부가 포함된 캘린더 정보를 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "월별 독서 캘린더를 성공적으로 조회했습니다.",
                    content = @Content(schema = @Schema(implementation = ReadingCalendarResponse.class))
            )
    })
    @GetMapping("/history/calendar")
    public ApiResponse<ReadingCalendarResponse> calendar(
            Authentication authentication,
            @Parameter(description = "조회할 연도입니다.", example = "2026")
            @RequestParam int year,
            @Parameter(description = "조회할 월입니다.", example = "5")
            @RequestParam int month
    ) {
        return ApiResponse.ok(readingService.calendar(authentication.getName(), year, month));
    }

    @Operation(
            summary = "날짜별 독서 기록 조회",
            description = "지정한 날짜에 해당하는 독서 기록과 책 목록을 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "날짜별 독서 기록을 성공적으로 조회했습니다.",
                    content = @Content(schema = @Schema(implementation = ReadingHistoryDateResponse.class))
            )
    })
    @GetMapping("/history/{date}")
    public ApiResponse<ReadingHistoryDateResponse> historyByDate(
            Authentication authentication,
            @Parameter(description = "조회할 날짜입니다.", example = "2026-05-18")
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ApiResponse.ok(readingService.historyByDate(authentication.getName(), date));
    }

    @Operation(
            summary = "독서 습관 조회",
            description = "현재 사용자의 연속 독서 일수와 습관 지표를 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "독서 습관 정보를 성공적으로 조회했습니다.",
                    content = @Content(schema = @Schema(implementation = HabbitResponse.class))
            )
    })
    @GetMapping("/habbit")
    public ApiResponse<HabbitResponse> habbit(Authentication authentication) {
        return ApiResponse.ok(readingService.getReadingHabbit(authentication.getName()));
    }
}
