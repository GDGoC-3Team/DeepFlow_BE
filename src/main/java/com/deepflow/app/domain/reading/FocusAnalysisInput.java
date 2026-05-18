package com.deepflow.app.domain.reading;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record FocusAnalysisInput(
        Long sessionId,
        LocalDate sessionDate,
        LocalDateTime completedAt,
        BookInput book,
        int currentOffset,
        int maxOffset,
        long totalReadingSeconds,
        List<Long> pageStayTimeDistribution,
        List<PageBehaviorInput> pages
) {

    public record BookInput(
            Long id,
            String title,
            String author
    ) {
    }

    public record PageBehaviorInput(
            int order,
            int pageIndex,
            int startOffset,
            int endOffset,
            int textAmount,
            long staySeconds,
            boolean movedBackward,
            boolean reread
    ) {
    }
}
