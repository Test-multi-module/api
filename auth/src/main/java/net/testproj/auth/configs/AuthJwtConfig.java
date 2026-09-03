package net.testproj.auth.configs;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import net.testproj.auth.properties.JwtProps;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;

@Configuration
public class AuthJwtConfig {
    private final ResourceLoader resourceLoader;

    public AuthJwtConfig(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Bean
    public JWKSet jwkSet(JwtProps props) throws Exception {
        JwtProps.KeystoreProps keystoreProps = props.getKeystore();
        String alias = keystoreProps.getKeyAlias();
        char[] keyPass = keystoreProps.getKeyPassword().toCharArray();

        KeyStore ks = KeyStore.getInstance(keystoreProps.getType());
        Resource resource = resourceLoader.getResource(keystoreProps.getLocation());

        try(InputStream is = resource.getInputStream()) {
            ks.load(is, keystoreProps.getStorePassword().toCharArray());
        }

        PrivateKey privateKey = (PrivateKey) ks.getKey(alias, keyPass);
        X509Certificate cert = (X509Certificate) ks.getCertificate(alias);
        RSAPublicKey publicKey = (RSAPublicKey) cert.getPublicKey();

        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID("rsa-key-1")
                .build();

        return new JWKSet(rsaKey);
    }

    @Bean
    public JwtEncoder jwtEncoder(JWKSet jwkSet) {
        var jwkSource = new ImmutableJWKSet<>(jwkSet);
        return new NimbusJwtEncoder(jwkSource);
    }
}
