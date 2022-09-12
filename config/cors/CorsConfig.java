package com.aim.questionnaire.config.cors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Created by lmy on 2017/12/20.
 *
 * 设置跨域请求
 */

@Configuration
public class CorsConfig {
    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfigurationppp = new CorsConfiguration();
        corsConfigurationppp.addAllowedOrigin("*"); // 1
        corsConfigurationppp.addAllowedHeader("*"); // 2
        corsConfigurationppp.addAllowedMethod("*"); // 3
        corsConfigurationppp.setAllowCredentials(true);
        return corsConfigurationppp;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource sourceppp = new UrlBasedCorsConfigurationSource();
        sourceppp.registerCorsConfiguration("/**", buildConfig()); // 4
        return new CorsFilter(sourceppp);
    }
}
