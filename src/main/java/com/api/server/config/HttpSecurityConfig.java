package com.api.server.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.api.server.config.filter.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class HttpSecurityConfig {

    @Autowired private AuthenticationProvider daoAuthProvider;
    @Autowired private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired private AuthenticationEntryPoint authenticationEntryPoint;
    @Autowired private AccessDeniedHandler accessDeniedHandler;
    @Autowired private AuthorizationManager<RequestAuthorizationContext> authorizationManager;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
            /* 1️⃣  CORS primero */
            .cors(Customizer.withDefaults())

            /* 2️⃣  Sin estado porque usas JWT */
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            /* 3️⃣  Autenticación */
            .authenticationProvider(daoAuthProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

            /* 4️⃣  AUTORIZACIÓN
                   - Dejamos pasar OPTIONS a todo
                   - El resto lo maneja tu AuthorizationManager
            */
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .anyRequest().access(authorizationManager)
            )

            /* 5️⃣  Manejadores de excepción */
            .exceptionHandling(ex -> {
                ex.authenticationEntryPoint(authenticationEntryPoint);
                ex.accessDeniedHandler(accessDeniedHandler);
            })

            .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        /*  ⬇⬇  Usa origin patterns para wildcard + credenciales  ⬇⬇  */
        config.setAllowedOriginPatterns(Arrays.asList("*"));   // o quita "*" y lista tus dominios
        // —— Si prefieres concretos, quita la línea de arriba y deja:
        // config.setAllowedOrigins(List.of("http://fammeba.duckdns.org"));

        config.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
