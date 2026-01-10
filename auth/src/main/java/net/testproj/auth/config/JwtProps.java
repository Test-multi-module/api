package net.testproj.auth.config;

import lombok.Getter;import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="auth.security.jwt.keystore")
@Getter
@Setter
public class JwtProps {
    private String location;
    private String storePassword;
    private String keyAlias;
    private String keyPassword;
}
