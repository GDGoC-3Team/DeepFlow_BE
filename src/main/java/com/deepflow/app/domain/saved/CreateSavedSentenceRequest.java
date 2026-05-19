package com.deepflow.app.domain.saved;

import com.deepflow.app.domain.settings.FontFamily;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "리딩 중 선택한 문장을 저장하는 요청입니다.")
public record CreateSavedSentenceRequest(
        @Schema(description = "선택한 문장 텍스트입니다.", example = "문장 하나가 마음을 움직인다.")
        @NotBlank(message = "selectedText is required")
        String selectedText,
        @Schema(description = "문장 카드에 사용할 배경 이미지 URL입니다.", example = "https://storage.googleapis.com/deepflow-image-storage/background-image/image_1.png")
        String imageUrl,
        @Schema(description = "저장할 글꼴입니다.", example = "NANUM_MYEONGJO")
        @NotNull(message = "fontFamily is required")
        FontFamily fontFamily,
        @Schema(description = "저장할 글자 크기입니다.", example = "18")
        @Min(value = 1, message = "fontSize must be greater than or equal to 1")
        int fontSize,
        @Schema(description = "선택 시작 오프셋입니다.", example = "120")
        @Min(value = 0, message = "startOffset must be greater than or equal to 0")
        int startOffset,
        @Schema(description = "선택 종료 오프셋입니다.", example = "156")
        @Min(value = 0, message = "endOffset must be greater than or equal to 0")
        int endOffset
) {
}
