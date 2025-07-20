package com.woohakdong.framework.config;

import static io.swagger.v3.oas.models.security.SecurityScheme.In.HEADER;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String LOCAL_HOST_URL = "http://localhost:8080";
    private static final String DEV_HOST_URL = "https://woohakdong.jalju.com";

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("Woohakdong Server API")
                .description("우학동 서버 API 명세서")
                .version("0.0.1");


        SecurityScheme securityScheme = new SecurityScheme()
                .name("Authorization")
                .type(Type.HTTP)
                .in(HEADER)
                .bearerFormat("JWT")
                .scheme("Bearer");

        Components components = new Components()
                .addSecuritySchemes("accessToken", securityScheme);

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("accessToken");

        Server localServer = new Server();
        localServer.setUrl(LOCAL_HOST_URL);
        localServer.setDescription("로컬 서버");

        Server devServer = new Server();
        devServer.setUrl(DEV_HOST_URL);
        devServer.setDescription("개발 서버");

        return new OpenAPI()
                .components(components)
                .servers(List.of(localServer, devServer))
                .addSecurityItem(securityRequirement)
                .info(info);
    }
}
