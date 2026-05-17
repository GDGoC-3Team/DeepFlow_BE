package com.deepflow.app.domain.reading;

import java.time.LocalDate;
import java.util.List;

public record ReadingHistoryDateResponse(LocalDate date, boolean completed, List<ReadingHistoryBookResponse> books) {
}
