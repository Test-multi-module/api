package net.testproj.auth.configs;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import net.testproj.auth.properties.AuthProps;
import net.testproj.auth.properties.JwtKeyStoreProps;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
@EnableConfigurationProperties({JwtKeyStoreProps.class, AuthProps.class})
public class AuthJwtConfig {
    private final ResourceLoader resourceLoader;

    public AuthJwtConfig(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Bean
    public JwtEncoder jwtEncoder(JwtKeyStoreProps props) throws Exception {
        String alias = props.getKeyAlias();
        char[] keyPass = props.getKeyPassword().toCharArray();

        KeyStore ks = KeyStore.getInstance(props.getType());

        Resource resource = resourceLoader.getResource(props.getLocation());

        try(InputStream is = resource.getInputStream()) {
            ks.load(is, props.getStorePassword().toCharArray());
        }

        PrivateKey privateKey = (PrivateKey) ks.getKey(alias, keyPass);
        X509Certificate cert = (X509Certificate) ks.getCertificate(alias);
        RSAPublicKey publicKey = (RSAPublicKey) cert.getPublicKey();

        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID("rsa-key-1")
                .build();

        var jwkSource = new ImmutableJWKSet<>(new JWKSet(rsaKey));
        return new NimbusJwtEncoder(jwkSource);
    }
}
