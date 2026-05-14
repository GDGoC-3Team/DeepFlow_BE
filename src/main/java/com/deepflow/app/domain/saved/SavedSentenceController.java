package com.deepflow.app.domain.saved;

import com.deepflow.app.common.ApiResponse;
import com.deepflow.app.domain.sentence.SentenceResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/my/sentences")
@RequiredArgsConstructor
public class SavedSentenceController {

    private final SavedSentenceService savedSentenceService;

    @GetMapping
    public ApiResponse<List<SentenceResponse>> mySentences(
            Authentication authentication,
            @RequestParam(defaultValue = "latest") String sort
    ) {
        return ApiResponse.ok(savedSentenceService.mySentences(authentication.getName(), sort));
    }
}
