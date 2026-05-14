package com.deepflow.app.domain.saved;

import com.deepflow.app.common.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/my/sentences")
@RequiredArgsConstructor
public class SavedSentenceController {

    private final SavedSentenceService savedSentenceService;

    // 저장한 문장 목록 조회
    @GetMapping
    public ApiResponse<List<SavedSentenceResponse>> mySentences(
            Authentication authentication,
            @RequestParam(defaultValue = "all") String type,
            @RequestParam(defaultValue = "latest") String sort
    ) {
        return ApiResponse.ok(savedSentenceService.mySentences(authentication.getName(), type, sort));
    }

    // 저장된 문장 삭제 (문장 자체 삭제 X, 유저의 저장 기록 여부만 삭제)
    @DeleteMapping("/{sentenceId}")
    public ApiResponse<Void> delete(Authentication authentication, @PathVariable Long sentenceId) {
        savedSentenceService.delete(authentication.getName(), sentenceId);
        return ApiResponse.ok(null, "Deleted");
    }
}
