package com.apiPersistence.dataPersistenceApi.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")          // changed from /api/** to /**
                        .allowedOriginPatterns("*") // use allowedOriginPatterns not allowedOrigins
                        .allowedMethods("GET", "POST", "DELETE", "OPTIONS", "PUT", "PATCH")
                        .allowedHeaders("*")
                        .exposedHeaders("*");
            }
        };
    }
}
