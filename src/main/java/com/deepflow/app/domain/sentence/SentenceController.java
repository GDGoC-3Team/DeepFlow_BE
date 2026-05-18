package com.deepflow.app.domain.sentence;

import com.deepflow.app.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "문장", description = "문장 피드 조회와 저장 기능을 제공하는 API입니다.")
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
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = SentenceResponse.class)))
            )
    })
    @GetMapping("/feed")
    public ApiResponse<List<SentenceResponse>> feed(
            @Parameter(description = "반환할 문장 개수입니다. 값을 보내지 않으면 10개를 조회합니다.", example = "10")
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.ok(sentenceService.randomFeed(size).stream().map(SentenceResponse::from).toList());
    }

    @Operation(
            summary = "문장 저장 또는 저장 해제",
            description = "현재 사용자의 저장 상태를 기준으로 문장을 저장하거나 저장 해제합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "문장 저장 상태를 성공적으로 변경했습니다.",
                    content = @Content(schema = @Schema(implementation = Boolean.class))
            )
    })
    @PostMapping("/{id}/save")
    public ApiResponse<Boolean> save(Authentication authentication, @PathVariable Long id) {
        return ApiResponse.ok(sentenceService.toggleSave(authentication.getName(), id));
    }
}
