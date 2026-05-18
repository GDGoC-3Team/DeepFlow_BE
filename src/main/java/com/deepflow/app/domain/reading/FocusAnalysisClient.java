package com.deepflow.app.domain.reading;

public interface FocusAnalysisClient {

    FocusAnalysisResponse analyze(Long sessionId, String systemPrompt, String userPrompt);
}
