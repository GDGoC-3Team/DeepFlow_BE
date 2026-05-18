package com.deepflow.app.domain.reading;

public class LlmAnalysisException extends RuntimeException {

    public LlmAnalysisException(String message) {
        super(message);
    }

    public LlmAnalysisException(String message, Throwable cause) {
        super(message, cause);
    }
}
