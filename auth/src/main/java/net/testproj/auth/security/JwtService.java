package net.testproj.auth.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.testproj.auth.config.JwtProps;
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
        long ttl = props.getAccessToken().getTtlSeconds();

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

    public String validateAndExtractUserName(String token) {
        try {
            return Jwts.parserBuilder()//.setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        }catch(MalformedJwtException e){//todo Custom exception and process it via ControllerAdvice
            throw new MalformedJwtException("TODO if needed MalformedJwtException processing");
        }catch(SignatureException e){
            throw new SignatureException("TODO if needed SignatureException processing");
        }catch (ExpiredJwtException e) {
            throw new JwtException("TODO if needed ExpiredJwtException processing");
        }
    }
}