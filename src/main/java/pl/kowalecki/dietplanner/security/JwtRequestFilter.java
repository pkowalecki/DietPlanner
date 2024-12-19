package pl.kowalecki.dietplanner.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.reactive.function.client.WebClient;
import pl.kowalecki.dietplanner.exception.GlobalControllerAdvice;
import pl.kowalecki.dietplanner.utils.AuthUtils;
import pl.kowalecki.dietplanner.utils.RouteUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static pl.kowalecki.dietplanner.utils.UrlTools.AUTH_SERVICE_URL;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final WebClient authorizationWebClient;
    private final AuthUtils authUtils;
    private final RouteUtils routeUtils;

    public JwtRequestFilter(WebClient.Builder webClientBuilder, AuthUtils authUtils, RouteUtils routeUtils) {
        this.authorizationWebClient = webClientBuilder.baseUrl(AUTH_SERVICE_URL).build();
        this.authUtils = authUtils;
        this.routeUtils = routeUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (routeUtils.isOpenPath(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = authUtils.extractAccessTokenFromRequest(request);

        if (accessToken != null) {
            try {
                authUtils.validateToken(accessToken);
                filterChain.doFilter(request, response);
            } catch (ExpiredJwtException e) {
                log.info("Access token expired. Attempting to refresh...");
                handleRefreshToken(request, response, filterChain);
            } catch (Exception e) {
                //FIXME Do fixu bo za dużo łapie. W tym błąd renderowania strony.
                log.error("Invalid JWT token: {}", e.getMessage());
                redirectToLogin(response);
            }
        } else {
            handleRefreshToken(request, response, filterChain);
        }
    }

    private void handleRefreshToken(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        log.warn("No JWT token found in request");
        String refreshToken = authUtils.extractRefreshTokenFromRequest(request);
        if (refreshToken != null) {
            try {
                Map<String, String> tokens = refreshAccessToken(refreshToken);
                authUtils.setAccessTokenCookie(response, tokens.get("accessToken"), 15 * 60);
                filterChain.doFilter(request, response);
            } catch (Exception ex) {
                log.error("Unable to refresh access token: {}", ex.getMessage());
                redirectToLogin(response);
            }
        } else {
            redirectToLogin(response);
        }
    }

    private Map<String, String> refreshAccessToken(String refreshToken) {
        return authorizationWebClient.post()
                .uri("/refresh")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + refreshToken)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {
                })
                .block();
    }

    private void redirectToLogin(HttpServletResponse response) {
        try {
            response.sendRedirect("/app/?sessionExpired=true");
        } catch (Exception e) {
            log.error("Unable to redirect to login");
        }
    }
}
