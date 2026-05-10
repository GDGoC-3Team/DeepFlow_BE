package com.deepflow.app.domain.reading;

public record PageTimeRequest(Long sessionId, int pageNumber, long elapsedSeconds) {
}
