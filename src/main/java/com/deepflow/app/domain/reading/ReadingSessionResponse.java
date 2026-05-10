package com.deepflow.app.domain.reading;

import com.deepflow.app.domain.book.BookResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ReadingSessionResponse(Long id, BookResponse book, LocalDate date, LocalDateTime completedAt) {

    public static ReadingSessionResponse from(ReadingSession session) {
        return new ReadingSessionResponse(
                session.getId(),
                BookResponse.from(session.getBook()),
                session.getDate(),
                session.getCompletedAt()
        );
    }
}
