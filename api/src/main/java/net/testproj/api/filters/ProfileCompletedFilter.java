package net.testproj.api.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import net.testproj.db.auth.AuthUserDS;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@AllArgsConstructor
public class ProfileCompletedFilter extends OncePerRequestFilter {

    private final AuthUserDS authUserDS;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        var requestUri = request.getRequestURI();
        if (requestUri.equals("/api/private/create-profile") ||
                !requestUri.startsWith("/api/private/")) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null)
            throw new IOException("""
                    TODO: currently there is no scenario where authentication == null,
                    but this filter should be revisited together with all security rules later,
                    once the business access architecture becomes clear.""");

        UUID userId = UUID.fromString(authentication.getName());
        boolean profileCompleted = authUserDS.isProfileCompleted(userId);

        if (!profileCompleted) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType("application/json");
            response.getWriter().write("""
        {"code":"USER_ONBOARDING_REQUIRED","message":"User profile must be completed"}
        """);
            return;
        }

        filterChain.doFilter(request, response);
    }
}