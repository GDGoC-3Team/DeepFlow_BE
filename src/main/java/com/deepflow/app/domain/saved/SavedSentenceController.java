package com.deepflow.app.domain.saved;

import com.deepflow.app.common.ApiResponse;
import com.deepflow.app.domain.sentence.SentenceResponse;
import com.deepflow.app.domain.user.User;
import com.deepflow.app.domain.user.UserService;
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

    private final SavedSentenceRepository savedSentenceRepository;
    private final UserService userService;

    @GetMapping
    public ApiResponse<List<SentenceResponse>> mySentences(
            Authentication authentication,
            @RequestParam(defaultValue = "latest") String sort
    ) {
        User user = userService.getCurrentUser(authentication.getName());
        List<SavedSentence> savedSentences = switch (sort) {
            case "date" -> savedSentenceRepository.findByUserOrderBySentenceDate(user);
            case "alphabet" -> savedSentenceRepository.findByUserOrderBySentenceContent(user);
            default -> savedSentenceRepository.findByUserOrderBySavedAtDesc(user);
        };
        return ApiResponse.ok(savedSentences.stream()
                .map(SavedSentence::getSentence)
                .map(SentenceResponse::from)
                .toList());
    }
}
