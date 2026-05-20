package com.deepflow.app.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Deepflow API 문서")
                        .version("v1")
                        .description("Deepflow 백엔드에서 제공하는 인증, 문장, 독서, 설정 API 문서입니다."))
                .tags(List.of(
                        new Tag().name("독서 세션").description("오늘의 글 조회, 독서 시작, 독서 완료 API입니다."),
                        new Tag().name("독서 중 형광펜, 문장 저장").description("독서 중 읽기 시간 기록, 하이라이트 문장 저장 API입니다."),
                        new Tag().name("독서 기록 조회").description("독서 결과, 집중도 분석, 날짜별 기록, 캘린더 조회 API입니다."),
                        new Tag().name("알림").description("설정 중 알림 수신, 시간, 토큰 관련 항목입니다."),
                        new Tag().name("일반 설정").description("설정 중 알림 외 일반 설정 항목입니다.")
                ));
    }
}
