package com.deepflow.app.domain.reading;

import java.util.List;

public record ReadingResultResponse(Long sessionId, double averageSecondsPerCharacter, List<PageBreakdown> pages) {

    public record PageBreakdown(int pageNumber, long elapsedSeconds, int characterCount, double secondsPerCharacter, boolean outlier, boolean reread) {
    }
}
