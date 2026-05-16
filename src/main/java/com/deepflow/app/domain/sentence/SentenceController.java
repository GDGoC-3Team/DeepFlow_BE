package com.deepflow.app.domain.sentence;

import com.deepflow.app.common.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sentences")
@RequiredArgsConstructor
public class SentenceController {

    private final SentenceService sentenceService;

    @GetMapping("/feed")
    public ApiResponse<List<SentenceResponse>> feed(@RequestParam(defaultValue = "10") int size) {
        return ApiResponse.ok(sentenceService.randomFeed(size).stream().map(SentenceResponse::from).toList());
    }

    @PostMapping("/{id}/save")
    public ApiResponse<Boolean> save(Authentication authentication, @PathVariable Long id) {
        return ApiResponse.ok(sentenceService.toggleSave(authentication.getName(), id));
    }
}
