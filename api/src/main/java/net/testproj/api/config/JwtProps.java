package net.testproj.api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="api.security.jwt")
@Getter @Setter
public class JwtProps {

    private String issuer;
    private String audience;

    private TrustStore truststore;

    @Getter@Setter
    public static class TrustStore {
        private String location;
        private String password;
        private String type;
        private String alias;
    }

}