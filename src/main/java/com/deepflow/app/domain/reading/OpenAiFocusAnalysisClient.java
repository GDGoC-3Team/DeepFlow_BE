package com.deepflow.app.domain.reading;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class OpenAiFocusAnalysisClient implements FocusAnalysisClient {

    private final ObjectMapper objectMapper;
    private final RestClient restClient;
    private final String apiKey;
    private final String model;

    public OpenAiFocusAnalysisClient(
            ObjectMapper objectMapper,
            @Value("${llm.openai.base-url:https://api.openai.com/v1}") String baseUrl,
            @Value("${llm.openai.api-key:}") String apiKey,
            @Value("${llm.openai.model:gpt-4o-mini}") String model
    ) {
        this.objectMapper = objectMapper;
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
        this.apiKey = apiKey;
        this.model = model;
    }

    //// =========================
    //// LLM 호출
    //// =========================
    @Override
    public FocusAnalysisResponse analyze(Long sessionId, String systemPrompt, String userPrompt) {
        if (!StringUtils.hasText(apiKey)) {
            throw new LlmAnalysisException("LLM API key is not configured");
        }

        // 1. OpenAI 요청 payload
        Map<String, Object> request = Map.of(
                "model", model,
                "temperature", 0.2,
                "response_format", Map.of("type", "json_object"),
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", userPrompt)
                )
        );

        try {
            String response = restClient.post()
                    .uri("/chat/completions")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(String.class);

            // 2. JSON 추출
            String content = extractContent(response);

            // 3. DTO 변환 후 반환
            return objectMapper.readValue(content, FocusAnalysisResponse.class);
        } catch (RestClientException exception) {
            throw new LlmAnalysisException("LLM focus analysis request failed", exception);
        } catch (JsonProcessingException exception) {
            throw new LlmAnalysisException("LLM focus analysis response was not valid JSON", exception);
        }
    }

    //// =========================
    //// OpenAI response 파싱
    //// =========================
    private String extractContent(String response) throws JsonProcessingException {
        if (!StringUtils.hasText(response)) {
            throw new LlmAnalysisException("LLM focus analysis response was empty");
        }
        JsonNode contentNode = objectMapper.readTree(response).path("choices").path(0).path("message").path("content");
        if (contentNode.isMissingNode() || !StringUtils.hasText(contentNode.asText())) {
            throw new LlmAnalysisException("LLM focus analysis content was empty");
        }
        return contentNode.asText();
    }
}
