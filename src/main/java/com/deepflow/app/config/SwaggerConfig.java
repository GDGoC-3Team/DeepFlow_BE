package com.deepflow.app.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
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
                        .description("Deepflow 백엔드에서 제공하는 인증, 문장, 독서, 설정 API 문서입니다."));
    }
}
