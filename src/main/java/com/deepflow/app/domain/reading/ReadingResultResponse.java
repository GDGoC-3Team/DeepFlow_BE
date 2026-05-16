package com.deepflow.app.domain.reading;

import java.util.List;

public record ReadingResultResponse(
        Long sessionId,
        int currentOffset,
        int maxOffset,
        double progressPercent,
        double averageSecondsPerCharacter,
        List<SegmentBreakdown> segments
) {

    public record SegmentBreakdown(
            int startOffset,
            int endOffset,
            long elapsedSeconds,
            int characterCount,
            double secondsPerCharacter,
            boolean outlier,
            boolean reread
    ) {
    }
}
