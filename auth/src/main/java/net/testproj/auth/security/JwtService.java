package net.testproj.auth.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.testproj.auth.properties.JwtProps;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collection;
import java.util.UUID;


@Service
@Getter
@EnableConfigurationProperties(JwtProps.class)
@RequiredArgsConstructor
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final JwtProps props;

    public String issueAccessToken(String subject, Collection<String> roles) {
        long ttl = props.getAccessTokenProps().getTtlSeconds();

        Instant now = Instant.now();
        Instant exp = now.plusSeconds(ttl);

        JwtClaimsSet.Builder claims = JwtClaimsSet.builder()
                .issuer(props.getIssuer())
                .subject(subject)
                .issuedAt(now)
                .expiresAt(exp)
                .id(UUID.randomUUID().toString());

        // todo aud

        if (roles != null && !roles.isEmpty()) {claims.claim("roles", roles);}

        JwsHeader jwsHeader = JwsHeader.with(SignatureAlgorithm.RS256).build();

        JwtEncoderParameters params = JwtEncoderParameters.from(jwsHeader, claims.build());
        return jwtEncoder.encode(params).getTokenValue();
    }
}