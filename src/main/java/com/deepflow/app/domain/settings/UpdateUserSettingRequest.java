package com.deepflow.app.domain.settings;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalTime;

public record UpdateUserSettingRequest(
        Boolean notificationEnabled,
        @JsonFormat(pattern = "HH:mm")
        LocalTime notificationTime,
        FontFamily fontFamily,
        @Min(8)
        @Max(48)
        Integer fontSize
) {
}
