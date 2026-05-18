package com.deepflow.app.domain.reading;

import java.util.List;

public record FocusAnalysisResponse(
        List<PageFocusResult> pageFocusResults
) {

    public record PageFocusResult(
            int pageIndex,
            int focusScore,
            String explanation
    ) {
    }
}
