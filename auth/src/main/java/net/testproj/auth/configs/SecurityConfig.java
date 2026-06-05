package net.testproj.auth.configs;

import net.testproj.auth.handlers.CustomOAuth2SuccessHandler;
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
@EnableConfigurationProperties(CorsProps.class)//todo переместить в application yml в блок auth
@AllArgsConstructor
public class SecurityConfig {
    private final CustomOAuth2SuccessHandler successHandler;
    private final CorsProps corsProps;

    @Bean
    public SecurityFilterChain authSecurityFilterChain(HttpSecurity http)  {
        return http
                .securityMatcher(//todo: не забывать обновлять матчер при необходимости
                        "/auth/**",
                        "/oauth2/**",
                        "/login/oauth2/**"
                )
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(
                                "/auth/exchange",
                                "/oauth2/authorization/**",
                                "/login/oauth2/code/**"
                        ).permitAll()
                        .anyRequest().denyAll())
                .cors(Customizer.withDefaults())//todo изучить что это и для чего оно  надо и почему мне достаточно дефолтов
                .csrf(AbstractHttpConfigurer::disable)//todo изучить что это и для чего оно бывает надо и почему мне не надо
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .oauth2Login(oauth2 -> oauth2.successHandler(successHandler))
                .build();
    }

    @Bean
    public CorsConfigurationSource authCorsConfigurationSource() {
        //todo : анализ и изучене, что надо что не надо теперь, когда этот фильтр чейн только под auth
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