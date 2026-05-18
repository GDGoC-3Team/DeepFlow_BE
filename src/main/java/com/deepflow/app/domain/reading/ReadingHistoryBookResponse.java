package com.deepflow.app.domain.reading;

import com.deepflow.app.domain.book.BookResponse;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "특정 날짜에 읽은 책 정보입니다.")
public record ReadingHistoryBookResponse(
        @Schema(description = "독서 세션 ID입니다.", example = "1")
        Long sessionId,
        @Schema(description = "책 정보입니다.")
        BookResponse book
) {

    public static ReadingHistoryBookResponse from(ReadingSession session) {
        return new ReadingHistoryBookResponse(
                session.getId(),
                BookResponse.from(session.getBook())
        );
    }
}
