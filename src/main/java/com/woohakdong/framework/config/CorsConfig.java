package com.woohakdong.framework.config;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * SecurityFilterChain에서 CORS 설정을 적용하기 위한 클래스입니다.
 */
public class CorsConfig {

    public static UrlBasedCorsConfigurationSource getCorsConfigSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("https://localhost:3000");
        config.addAllowedOrigin("https://localhost:3001");
        config.addAllowedOrigin("https://local.woohakdong.com:3000");
        config.addAllowedOrigin("https://local.woohakdong.com:3001");
        config.addAllowedOrigin("https://localhost:3000");
        config.addAllowedOrigin("https://localhost:3001");
        config.addAllowedOrigin("https://www.woohakdong.com");
        config.addAllowedOrigin("https://app.woohakdong.com");
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        configurationSource.registerCorsConfiguration("/**", config);
        return configurationSource;
    }
}
