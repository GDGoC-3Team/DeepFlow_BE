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
                        new Tag().name("독서 세션 진행").description("오늘의 읽기 추천, 세션 시작과 완료, 읽기 시간 기록, 하이라이트 생성과 삭제 API입니다."),
                        new Tag().name("독서 기록 조회").description("하이라이트 목록과 날짜별, 월별 독서 기록을 조회하는 API입니다."),
                        new Tag().name("독서 통계 조회").description("독서 결과, 집중도 분석, 독서 습관 통계를 조회하는 API입니다.")
                ));
    }
}
