package com.deepflow.app.domain.reading;

import com.deepflow.app.common.ApiResponse;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reading")
@RequiredArgsConstructor
public class ReadingController {

    private final ReadingService readingService;

    @PostMapping("/start")
    public ApiResponse<ReadingSessionResponse> start(Authentication authentication, @Valid @RequestBody StartReadingRequest request) {
        return ApiResponse.ok(ReadingSessionResponse.from(readingService.startSession(authentication.getName(), request.bookId())));
    }

    @PostMapping("/page-time")
    public ApiResponse<Void> pageTime(Authentication authentication, @Valid @RequestBody PageTimeRequest request) {
        readingService.recordPageTime(authentication.getName(), request);
        return ApiResponse.ok(null, "Recorded");
    }

    @GetMapping("/result/{sessionId}")
    public ApiResponse<ReadingResultResponse> result(Authentication authentication, @PathVariable Long sessionId) {
        return ApiResponse.ok(readingService.result(authentication.getName(), sessionId));
    }

    @GetMapping("/history/dates")
    public ApiResponse<List<LocalDate>> completedDates(Authentication authentication) {
        return ApiResponse.ok(readingService.completedDates(authentication.getName()));
    }

    @GetMapping("/habbit")
    public ApiResponse<HabbitResponse> habbit(Authentication authentication) {
        return ApiResponse.ok(readingService.getReadingHabbit(authentication.getName()));
    }
}
