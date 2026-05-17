package com.deepflow.app.domain.sentence;

import com.deepflow.app.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HomeController {

    private final SentenceService sentenceService;

    @Operation(
            summary = "Get daily home feed",
            description = "Returns 10 image-and-sentence cards for the given date. The same date returns the same feed ordering."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Home feed fetched successfully",
                    content = @Content(schema = @Schema(implementation = HomeFeedResponse.class))
            )
    })
    @GetMapping("/")
    public ApiResponse<HomeFeedResponse> feed(
            @Parameter(description = "Feed date. Defaults to today.", example = "2026-05-18")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @Parameter(description = "Number of cards to return. Defaults to 10.", example = "10")
            @RequestParam(defaultValue = "10") int size
    ) {
        LocalDate feedDate = date != null ? date : LocalDate.now();
        return ApiResponse.ok(sentenceService.getHomeFeed(feedDate, size));
    }
}
