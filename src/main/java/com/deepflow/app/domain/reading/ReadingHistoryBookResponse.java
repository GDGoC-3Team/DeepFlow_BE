package com.deepflow.app.domain.reading;

import com.deepflow.app.domain.book.BookResponse;

public record ReadingHistoryBookResponse(
        Long sessionId,
        BookResponse book
) {

    public static ReadingHistoryBookResponse from(ReadingSession session) {
        return new ReadingHistoryBookResponse(
                session.getId(),
                BookResponse.from(session.getBook())
        );
    }
}
