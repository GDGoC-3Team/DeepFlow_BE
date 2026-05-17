package com.deepflow.app.domain.reading;

import java.time.LocalDate;
import java.util.List;

public record ReadingCalendarResponse(int year, int month, LocalDate today, List<ReadingCalendarDayResponse> days) {
}
