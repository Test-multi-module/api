package net.testproj.api.config;

import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.oauth2.jwt.JwtDecoder;import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

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
        //todo;
        return new NimbusJwtDecoder(new DefaultJWTProcessor<>());
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