package com.deepflow.app.domain.sentence;

import com.deepflow.app.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "홈 문장", description = "홈 화면에 노출되는 문장 피드 관련 API입니다.")
public class HomeController {

    private final SentenceService sentenceService;

    @Operation(
            summary = "홈 문장 피드 조회",
            description = "지정한 날짜를 기준으로 홈 화면에 표시할 문장 카드 목록을 조회합니다. 같은 날짜를 요청하면 동일한 순서로 응답합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "홈 문장 피드를 성공적으로 조회했습니다.",
                    content = @Content(schema = @Schema(implementation = HomeFeedResponse.class))
            )
    })
    @GetMapping("/")
    public ApiResponse<HomeFeedResponse> feed(
            @Parameter(description = "피드를 조회할 날짜입니다. 값을 보내지 않으면 오늘 날짜를 사용합니다.", example = "2026-05-18")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @Parameter(description = "반환할 문장 카드 개수입니다. 값을 보내지 않으면 10개를 조회합니다.", example = "10")
            @RequestParam(defaultValue = "10") int size
    ) {
        LocalDate feedDate = date != null ? date : LocalDate.now();
        return ApiResponse.ok(sentenceService.getHomeFeed(feedDate, size));
    }
}
