package com.deepflow.app.domain.reading;

import com.deepflow.app.domain.book.BookResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "독서 세션 정보입니다.")
public record ReadingSessionResponse(
        @Schema(description = "독서 세션 ID입니다.", example = "1")
        Long id,
        @Schema(description = "독서 중인 책 정보입니다.")
        BookResponse book,
        @Schema(description = "독서 세션이 생성된 날짜입니다.", example = "2026-05-18")
        LocalDate date,
        @Schema(description = "독서를 완료한 시각입니다. 아직 완료되지 않았다면 null입니다.", example = "2026-05-18T21:30:00")
        LocalDateTime completedAt,
        @Schema(description = "현재 읽은 마지막 위치입니다.", example = "320")
        int currentOffset,
        @Schema(description = "현재까지 읽은 최대 위치입니다.", example = "540")
        int maxOffset
) {

    public static ReadingSessionResponse from(ReadingSession session) {
        return new ReadingSessionResponse(
                session.getId(),
                BookResponse.from(session.getBook()),
                session.getDate(),
                session.getCompletedAt(),
                session.getCurrentOffset(),
                session.getMaxOffset()
        );
    }
}
