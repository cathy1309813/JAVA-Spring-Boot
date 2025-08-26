package com.gtalent.demo.configs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title("tutor apis 範例") // 顯示在 Swagger UI 頁面標題
                        .description("Swagger documentation description for Gtelant classroom test") // 文件描述
                        .version("v1.0.1") // API 文件版本號
                        .license(new License().name("your license")) // 授權資訊 (可放 MIT, Apache 2.0 等)
                )
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .name("bearerAuth")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                        )
                );
    }

    @Bean
    public GroupedOpenApi groupedOpenApi() {
        return GroupedOpenApi.builder().group("public-apis")
                .pathsToMatch("/jwt/**",
                        "/v2/users/**",
                        "/products/**",
                        "/suppliers/**")
                .build();
    }
}
