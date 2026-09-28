package net.testproj.test.auth.utils;

import lombok.RequiredArgsConstructor;
import net.testproj.auth.properties.JwtProps;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;

@TestConfiguration
@RequiredArgsConstructor
public class TestAuthJwtConfig {

    private final ResourceLoader resourceLoader;
    private final JwtProps props;

    @Bean
    public RSAPublicKey testRsaPublicKey() throws Exception {
        JwtProps.KeystoreProps keystoreProps = props.getKeystore();
        KeyStore ks = KeyStore.getInstance(keystoreProps.getType());

        Resource resource = resourceLoader.getResource(keystoreProps.getLocation());
        try(InputStream is = resource.getInputStream()) {
            ks.load(is, keystoreProps.getStorePassword().toCharArray());
        }

        X509Certificate cert = (X509Certificate) ks.getCertificate(keystoreProps.getKeyAlias());
        return (RSAPublicKey) cert.getPublicKey();
    }

    @Bean
    public JwtDecoder jwtDecoder(RSAPublicKey testRsaPublicKey) {
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withPublicKey(testRsaPublicKey).build();
        decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>());
        return decoder;
    }
}
