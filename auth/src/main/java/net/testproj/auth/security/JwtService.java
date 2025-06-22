package net.testproj.auth.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.security.Keys;


@Service
@Getter
public class JwtService {

    private static final String SECRET_KEY = "mysupersecretkeywithnormallengthstartingfrom32";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour
    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public String generateToken(String usedId) {
        return Jwts.builder()
                .setSubject(usedId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String validateAndExtractUserName(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
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