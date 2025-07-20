package com.woohakdong.framework.config;

import static io.swagger.v3.oas.models.security.SecurityScheme.In.HEADER;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.servers.Server;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String LOCAL_HOST_URL = "http://localhost:8080";
    private static final String DEV_HOST_URL = "https://woohakdong.jalju.com";

    /**
     * 보안을 적용하지 않을 API 목록을 정의합니다. 컨트롤러 이름, 메서드 이름을 키와 값으로 사용합니다.
     * <p>
     * "AnotherController", List.of("publicMethod1","publicMethod2")
     */
    private static final Map<String, List<String>> EXEMPT_APIS = Map.of(
            "AuthController", List.of("socialLogin")
    );

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("Woohakdong Server API")
                .description("우학동 서버 API 명세서")
                .version("1.0.0");

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
                .addSecurityItem(securityRequirement)
                .servers(List.of(localServer, devServer))
                .info(info);
    }

    /**
     * 특정 API의 security 요구사항을 제거하는 커스터마이저입니다.
     */
    @Bean
    public OperationCustomizer removeSecurity() {
        return (operation, handlerMethod) -> {
            String controllerName = handlerMethod.getBeanType().getSimpleName();
            String methodName = handlerMethod.getMethod().getName();

            if (EXEMPT_APIS.containsKey(controllerName) && EXEMPT_APIS.get(controllerName).contains(methodName)) {
                operation.setSecurity(new ArrayList<>());
            }

            return operation;
        };
    }
}
