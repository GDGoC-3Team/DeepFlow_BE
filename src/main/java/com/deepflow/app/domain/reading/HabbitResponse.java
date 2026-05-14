package com.deepflow.app.domain.reading;

import com.fasterxml.jackson.annotation.JsonProperty;

public record HabbitResponse(
        @JsonProperty("streak_days")
        int streakDays,

        @JsonProperty("display_blocks")
        int displayBlocks
) {
}
