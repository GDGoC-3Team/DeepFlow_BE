package com.deepflow.app.domain.sentence;

import com.deepflow.app.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sentences")
@RequiredArgsConstructor
@Tag(name = "Sentences", description = "문장 피드 조회 및 저장 API입니다.")
public class SentenceController {

    private final SentenceService sentenceService;

    @Operation(
            summary = "문장 피드 조회",
            description = "랜덤 문장 피드를 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "문장 피드를 성공적으로 조회했습니다.",
                    content = @Content(schema = @Schema(implementation = SentenceFeedResponse.class))
            )
    })
    @GetMapping("/feed")
    public ApiResponse<SentenceFeedResponse> feed(
            @Parameter(description = "반환할 문장 개수입니다.", example = "10")
            @RequestParam(defaultValue = "10") int size
    ) {
        List<SentenceResponse> items = sentenceService.randomFeed(size).stream()
                .map(SentenceResponse::from)
                .toList();
        return ApiResponse.ok(new SentenceFeedResponse(LocalDate.now(), items));
    }

    @Operation(
            summary = "문장 저장 상태 토글",
            description = "고정 사용자(id=1) 기준으로 문장을 저장하거나 저장 해제합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "문장 저장 상태를 성공적으로 변경했습니다.",
                    content = @Content(schema = @Schema(implementation = Boolean.class))
            )
    })
    @PostMapping("/{id}/save")
    public ApiResponse<Boolean> save(@PathVariable Long id) {
        return ApiResponse.ok(sentenceService.toggleSave(id));
    }
}
