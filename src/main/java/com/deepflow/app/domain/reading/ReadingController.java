package com.deepflow.app.domain.reading;

import com.deepflow.app.common.ApiResponse;
import com.deepflow.app.domain.user.User;
import com.deepflow.app.domain.user.UserService;
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
    private final UserService userService;

    @PostMapping("/start")
    public ApiResponse<ReadingSessionResponse> start(Authentication authentication, @Valid @RequestBody StartReadingRequest request) {
        User user = userService.getCurrentUser(authentication);
        return ApiResponse.ok(ReadingSessionResponse.from(readingService.startSession(user, request.bookId())));
    }

    @PostMapping("/page-time")
    public ApiResponse<Void> pageTime(Authentication authentication, @Valid @RequestBody PageTimeRequest request) {
        User user = userService.getCurrentUser(authentication);
        readingService.recordPageTime(user, request);
        return ApiResponse.ok(null, "Recorded");
    }

    @GetMapping("/result/{sessionId}")
    public ApiResponse<ReadingResultResponse> result(Authentication authentication, @PathVariable Long sessionId) {
        User user = userService.getCurrentUser(authentication);
        return ApiResponse.ok(readingService.result(user, sessionId));
    }

    @GetMapping("/history/dates")
    public ApiResponse<List<LocalDate>> completedDates(Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        return ApiResponse.ok(readingService.completedDates(user));
    }

    @GetMapping("/habit-streak")
    public ApiResponse<Integer> habitStreak(Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        return ApiResponse.ok(readingService.habitStreak(user));
    }
}
