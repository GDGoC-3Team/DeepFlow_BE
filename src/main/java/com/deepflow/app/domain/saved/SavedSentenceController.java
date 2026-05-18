package com.deepflow.app.domain.saved;

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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/my/sentences")
@RequiredArgsConstructor
@Tag(name = "저장 문장", description = "사용자가 저장한 문장을 조회하고 삭제하는 API입니다.")
public class SavedSentenceController {

    private final SavedSentenceService savedSentenceService;

    @Operation(
            summary = "저장한 문장 목록 조회",
            description = "현재 사용자가 저장한 문장 목록을 조건에 따라 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "저장한 문장 목록을 성공적으로 조회했습니다.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = SavedSentenceResponse.class)))
            )
    })
    @GetMapping
    public ApiResponse<List<SavedSentenceResponse>> mySentences(
            Authentication authentication,
            @Parameter(description = "조회할 문장 유형입니다. 기본값은 all입니다.", example = "all")
            @RequestParam(defaultValue = "all") String type,
            @Parameter(description = "정렬 기준입니다. 기본값은 latest입니다.", example = "latest")
            @RequestParam(defaultValue = "latest") String sort
    ) {
        return ApiResponse.ok(savedSentenceService.mySentences(authentication.getName(), type, sort));
    }

    @Operation(
            summary = "저장한 문장 삭제",
            description = "현재 사용자가 저장한 문장을 목록에서 제거합니다. 문장 원본 데이터는 삭제되지 않습니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "저장한 문장을 성공적으로 삭제했습니다."
            )
    })
    @DeleteMapping("/{sentenceId}")
    public ApiResponse<Void> delete(Authentication authentication, @PathVariable Long sentenceId) {
        savedSentenceService.delete(authentication.getName(), sentenceId);
        return ApiResponse.ok(null, "Deleted");
    }
}
