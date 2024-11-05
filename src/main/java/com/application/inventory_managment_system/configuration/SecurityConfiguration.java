package com.application.inventory_managment_system.configuration;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import com.application.inventory_managment_system.converter.GrantedAuthorityConverter;

@EnableMethodSecurity
@Configuration
public class SecurityConfiguration {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception { // Метод задающий настройки Security
                                                                          // приложенгия
        return http
                .csrf(AbstractHttpConfigurer::disable) // CSFR(Cross Site Fake Request) По сокльку пользователь не
                                                       // взаимодействует с формами на прямую, а использует токен -
                                                       // отключаем.
                .cors(cors -> cors.configurationSource(corsConfigurationSource  )) // CORS(Cross-Origin Resource Sharing) поддержка вызова API с
                                                       // нескольких доменов. Отключаем, потому что настраиваем в
                                                       // WebConfig.
                .sessionManagement(
                        sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Устанавливаем
                                                                                                                       // политику
                                                                                                                       // управления
                                                                                                                       // сессиями
                                                                                                                       // как
                                                                                                                       // STATELESS,
                                                                                                                       // потому
                                                                                                                       // что
                                                                                                                       // состояние
                                                                                                                       // сессий
                                                                                                                       // храниться
                                                                                                                       // на
                                                                                                                       // клиенте.
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()) // Разрешить все виды запросов к API
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> { // Создание объекта кастомного конвертера Jwt в
                                                                    // GrantedAuthorities и установка его в настройку
                                                                    // OAuth2ResurceServer
                    JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
                    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new GrantedAuthorityConverter());
                    jwt.jwtAuthenticationConverter(jwtAuthenticationConverter);
                }))
                .build();
    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}
