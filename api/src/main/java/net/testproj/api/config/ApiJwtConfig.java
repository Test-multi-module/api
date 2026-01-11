package net.testproj.api.config;

import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;import org.springframework.security.oauth2.jwt.JwtDecoder;import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
@EnableConfigurationProperties(JwtProps.class)
public class ApiJwtConfig {
    @Bean
    public JwtDecoder jwtDecoder(JwtProps props) throws Exception {
        //todo;
        return new NimbusJwtDecoder(new DefaultJWTProcessor<>());
    }
}