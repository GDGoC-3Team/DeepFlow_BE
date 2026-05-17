package com.deepflow.app.domain.reading;

import java.time.LocalDate;

public record ReadingCalendarDayResponse(LocalDate date, boolean completed, boolean today) {
}
