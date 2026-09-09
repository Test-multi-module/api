package net.testproj.auth.handlers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import net.testproj.auth.properties.OAuth2LoginProps;
import net.testproj.auth.services.LoginCodeService;
import net.testproj.db.auth.*;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@AllArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final LoginCodeService loginCodeService;

    private final OAuth2AccountDS oAuth2AccountDS;
    private final AuthUserDS authUserDS;
    private final OAuth2LoginProps oAuth2LoginProps;


    @Override
    public void onAuthenticationSuccess(@NonNull HttpServletRequest request,
                                        @NonNull HttpServletResponse response,
                                        @NonNull Authentication authentication) throws IOException {
        if (!(authentication instanceof OAuth2AuthenticationToken token)) {
            throw new IllegalStateException("Expected OAuth2AuthenticationToken but got: " + authentication.getClass());
        }

        String provider = token.getAuthorizedClientRegistrationId();

        OAuth2User oAuth2User = token.getPrincipal();
        if (!(oAuth2User instanceof OidcUser oidcUser)) {
            throw new IllegalStateException("Expected OIDC user");
        }

        if(!Boolean.TRUE.equals(oidcUser.getEmailVerified())){
            throw new OAuth2AuthenticationException("Email not verified");
        }

        String providerUserId = oidcUser.getSubject();
        String email = oidcUser.getEmail();
        String familyName = oidcUser.getFamilyName();
        String givenName = oidcUser.getGivenName();
        String pictureUrl = oidcUser.getPicture();

        OAuth2Account oAuth2Account = oAuth2AccountDS.getByProviderUserIdAndProvider(providerUserId, provider);
        if (oAuth2Account != null) {
            oAuth2AccountDS.update(oAuth2Account.getId(), email, givenName, familyName);
        } else {
            AuthUser authUser = AuthUser.builder()
                    .emailVerified(Boolean.TRUE)
                    .email(email)
                    .build();

            authUser = authUserDS.insert(authUser);
            oAuth2Account = OAuth2Account.builder()
                    .userId(authUser.getId())
                    .provider(provider)
                    .providerUserId(providerUserId)
                    .emailAtProvider(email)
                    .providerAvatarUrl(pictureUrl)
                    .givenName(givenName)
                    .familyName(familyName)
                    .build();
            oAuth2AccountDS.insert(oAuth2Account);
        }

        String loginCode = loginCodeService.issue(oAuth2Account.getUserId());
        //test-case: что происходит с "зависшим логином.регистрацией" (когда код необменян из-за ошибки - поможет ли повторный вход)

        String redirectUrl = UriComponentsBuilder
                .fromUriString(oAuth2LoginProps.getRedirectUrl())
                .queryParam("code", loginCode)
                .build(true)//говорит Spring’у:«НЕ трогай и НЕ перекодируй уже готовые части URL».
                .toUriString();

        response.sendRedirect(redirectUrl);//cам ставит 302 статус и locationHeader
    }
}