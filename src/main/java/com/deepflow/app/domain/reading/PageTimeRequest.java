package com.deepflow.app.domain.reading;

public record PageTimeRequest(Long sessionId, int startOffset, int endOffset, long elapsedSeconds) {
}
