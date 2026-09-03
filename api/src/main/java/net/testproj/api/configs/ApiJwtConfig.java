package net.testproj.api.configs;

import net.testproj.api.properties.JwtProps;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;


@Configuration
public class ApiJwtConfig {
    @Bean
    public JwtDecoder jwtDecoder(JwtProps props) {
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withJwkSetUri(props.getJwkSetUri()).build();
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(props.getIssuer());
        OAuth2TokenValidator<Jwt> withAudience = audienceValidator(props.getAudience());

        decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(withIssuer, withAudience));
        return decoder;
    }

    private OAuth2TokenValidator<Jwt> audienceValidator(String audience) {
        return jwt -> {
            if (jwt.getAudience().contains(audience)) {return OAuth2TokenValidatorResult.success();}
            OAuth2Error error =
                    new OAuth2Error("invalid_token", "The required audience is missing", null);
            return OAuth2TokenValidatorResult.failure(error);
        };
    }
}