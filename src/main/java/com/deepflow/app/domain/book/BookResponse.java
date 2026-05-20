package com.deepflow.app.domain.book;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "책 정보입니다.")
public record BookResponse(
        @Schema(description = "책 ID입니다.", example = "3")
        Long id,
        @Schema(description = "책 제목입니다.", example = "어린 왕자")
        String title,
        @Schema(description = "저자명입니다.", example = "앙투안 드 생텍쥐페리")
        String author,
        @Schema(description = "표지 이미지 URL입니다.", example = "https://storage.googleapis.com/deepflow/books/little-prince.jpg")
        String coverImageUrl
) {

    public static BookResponse from(Book book) {
        return new BookResponse(book.getId(), book.getTitle(), book.getAuthor(), book.getCoverImageUrl());
    }
}
