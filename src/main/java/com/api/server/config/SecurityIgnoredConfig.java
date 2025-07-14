package com.api.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

@Configuration
public class SecurityIgnoredConfig {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                           .requestMatchers("/files/**")    // Para servir archivos estáticos
                           .requestMatchers("/api/designs") // Para el endpoint de guardado
                           .requestMatchers("/api/**");     // O ignora toda la API si es necesario
    }
}