package net.testproj.api.configs;

import net.testproj.api.properties.JwtProps;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPublicKey;

@Configuration
@EnableConfigurationProperties(JwtProps.class)
public class ApiJwtConfig {
    private final ResourceLoader resourceLoader;

    public ApiJwtConfig(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Bean
    public JwtDecoder jwtDecoder(JwtProps props) throws Exception {
        RSAPublicKey publicKey = loadRsaPublicKeyFromTruststore(props);
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withPublicKey(publicKey).build();

        String issuer = props.getIssuer();
        if (issuer == null || issuer.isBlank()) {
            throw new IllegalStateException("JWT issuer must be configured (api.security.jwt.issuer)");
        }

        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
        // todo audience

        decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(withIssuer));
        return decoder;
    }

    private RSAPublicKey loadRsaPublicKeyFromTruststore(JwtProps props) throws Exception {
        //todo check issuer and aud + хз может сонар куб подрубить к проекту
        JwtProps.TrustStore ts = props.getTruststore();

        KeyStore ks = KeyStore.getInstance(ts.getType());

        Resource resource = resourceLoader.getResource(ts.getLocation());

        try (InputStream is = resource.getInputStream()) {
            ks.load(is, ts.getStorePassword().toCharArray());
        }

        Certificate cert = ks.getCertificate(ts.getAlias());

        if (cert == null)
            throw new IllegalStateException("No certificate for alias: " + ts.getAlias());

        PublicKey pk = cert.getPublicKey();

        if (!(pk instanceof RSAPublicKey rsa))
            throw new IllegalStateException("Public key is not RSA. Actual: " + pk.getAlgorithm());

        return rsa;
    }
}