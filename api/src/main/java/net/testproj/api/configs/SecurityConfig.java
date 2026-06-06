package net.testproj.api.configs;

import lombok.AllArgsConstructor;
import net.testproj.auth.properties.CorsProps;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@Configuration
@EnableMethodSecurity
@EnableConfigurationProperties(CorsProps.class)//todo проследить что бы тут были только api настройки
@AllArgsConstructor
public class SecurityConfig {
    private final CorsProps corsProps;


    @Bean
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http)  {
        //todo : анализ и изучене, что надо что не надо теперь, когда этот фильтр чейн только под api
        return http
                .securityMatcher("/api/**")
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/private/**").authenticated()
                        .anyRequest().permitAll())
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .build();
    }

    @Bean
    public CorsConfigurationSource apiCorsConfigurationSource() {
        //todo : анализ и изучене, что надо что не надо теперь, когда этот фильтр чейн только под api
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(corsProps.getAllowedOrigins());
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}