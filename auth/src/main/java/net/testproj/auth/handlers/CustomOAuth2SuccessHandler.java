package net.testproj.auth.handlers;

import net.testproj.auth.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@AllArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        if (!(authentication instanceof OAuth2AuthenticationToken token)) {
            throw new IllegalStateException("Expected OAuth2AuthenticationToken but got: " + authentication.getClass());
        }

        String provider = token.getAuthorizedClientRegistrationId();

        OAuth2User user = token.getPrincipal();
        Map<String, Object> attrs = user.getAttributes();

        String providerUserId = String.valueOf(attrs.get("sub"));
        String email = String.valueOf(attrs.get("email"));
        String givenName = (String) attrs.get("given_name");
        String familyName = (String) attrs.get("family_name");
        String pictureUrl = (String) attrs.get("picture");

        System.out.println("OAuth2 attrs keys: " + attrs.keySet());


        // тут можно создать пользователя в БД и выдать JWT String email = user.getAttribute("email");
        // выдать JWT:
        String jwt = jwtService.generateToken("todo generation and all process here");

        // вернуть в тело или через redirect
        response.getWriter().write(jwt);
    }
}
