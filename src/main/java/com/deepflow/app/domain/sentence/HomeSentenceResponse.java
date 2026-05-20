package com.deepflow.app.domain.sentence;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "홈 화면에 표시되는 문장 항목입니다.")
public record HomeSentenceResponse(
        @Schema(description = "문장 내용입니다.", example = "책이 없는 방은 영혼 없는 육체와 같다.")
        String content,
        @Schema(description = "문장 이미지 URL입니다.", example = "https://storage.googleapis.com/deepflow/sentences/example.jpg")
        String imageUrl,
        @Schema(description = "책 제목입니다.", example = "선집")
        String bookTitle,
        @Schema(description = "저자명입니다.", example = "마르쿠스 툴리우스 키케로")
        String author
) {

    public static HomeSentenceResponse from(Sentence sentence) {
        return new HomeSentenceResponse(
                sentence.getContent(),
                sentence.getImageUrl(),
                sentence.getBookTitle(),
                sentence.getAuthor()
        );
    }
}
