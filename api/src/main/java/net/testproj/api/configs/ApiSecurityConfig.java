package net.testproj.api.configs;

import lombok.AllArgsConstructor;
import net.testproj.api.filters.ProfileCompletedFilter;
import net.testproj.auth.properties.CorsProps;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@Configuration
@EnableMethodSecurity
@EnableConfigurationProperties(CorsProps.class)//todo проследить что бы тут были только api настройки
@AllArgsConstructor
public class ApiSecurityConfig {
    private final CorsProps corsProps;
    private final ProfileCompletedFilter profileCompletedFilter;


    @Bean
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http)  {
        return http
                .securityMatcher("/api/**")
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/private/**").authenticated()
                        .anyRequest().permitAll())
                .cors(cors -> cors.configurationSource(apiCorsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .addFilterAfter(profileCompletedFilter, BearerTokenAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource apiCorsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(corsProps.getAllowedOrigins());//клиенты, которым разрешен доступ к api
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));//это какие HTTP methods клиенту разрешено использовать в cross-origin request.
        configuration.setAllowedHeaders(List.of("*"));//какие request headers клиенту разрешено присылать с frontend-запросом.
        configuration.setExposedHeaders(List.of("Authorization", "Content-Type"));//информация для браузера, какие заголовки он может позволить читать js-коду  из ответов сервера

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}