package com.deepflow.app.domain.reading;

import com.deepflow.app.domain.book.Book;
import com.deepflow.app.domain.book.BookPage;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "오늘의 읽기 추천 응답입니다.")
public record TodayReadingResponse(
        @Schema(description = "책 ID입니다.", example = "12")
        Long bookId,
        @Schema(description = "책 제목입니다.", example = "어린 왕자")
        String title,
        @Schema(description = "저자명입니다.", example = "앙투안 드 생텍쥐페리")
        String author,
        @Schema(description = "책 표지 이미지 URL입니다.", example = "https://storage.googleapis.com/deepflow/books/little-prince.jpg")
        String coverImageUrl,
        @Schema(description = "오늘 읽기에 제공할 본문 내용입니다.", example = "사막은 아름다워. 어딘가에 샘을 숨기고 있으니까.")
        String content,
        @Schema(description = "본문의 글자 수입니다.", example = "34")
        int characterCount
) {

    public static TodayReadingResponse from(Book book, BookPage bookPage) {
        return new TodayReadingResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getCoverImageUrl(),
                bookPage.getContent(),
                bookPage.getCharacterCount()
        );
    }
}
