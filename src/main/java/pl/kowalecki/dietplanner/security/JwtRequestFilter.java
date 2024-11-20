package pl.kowalecki.dietplanner.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.reactive.function.client.WebClient;
import pl.kowalecki.dietplanner.utils.JwtUtil;

import java.io.IOException;
import java.util.List;

import static pl.kowalecki.dietplanner.utils.UrlTools.AUTH_SERVICE_URL;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtils;
    private final WebClient authorizationWebClient;

    public JwtRequestFilter(JwtUtil jwtUtils, WebClient.Builder webClientBuilder) {
        this.jwtUtils = jwtUtils;
        this.authorizationWebClient = webClientBuilder.baseUrl(AUTH_SERVICE_URL+"/refresh").build();
    }
    @Value("${dietplanner.app.jwtCookieName}")
    String jwtName;
    @Value("${dietplanner.app.jwtRefreshCookieName}")
    String jwtRefName;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {



        List<String> openPaths = List.of(
                "/",
                "/app/",
                "/app/login",
                "/app/registerModal"
        );

        if (openPaths.stream().anyMatch(request.getRequestURI()::equals) || request.getRequestURI().startsWith("/static/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = jwtUtils.extractTokenFromRequest(request, jwtName);

        if (accessToken != null) {
            try {
                jwtUtils.isTokenValid(accessToken);
            } catch (ExpiredJwtException e) {
                log.info("Access token expired. Attempting to refresh...");

                String refreshToken = jwtUtils.extractTokenFromRequest(request, jwtRefName);

                if (refreshToken != null) {
                    try {
                        String newAccessToken = refreshAccessToken(refreshToken);

                        ResponseCookie newAccessTokenCookie = ResponseCookie.from("accessToken", newAccessToken)
                                .path("/")
                                .httpOnly(true)
                                .secure(true)
                                .build();
                        response.addHeader(HttpHeaders.SET_COOKIE, newAccessTokenCookie.toString());

                        filterChain.doFilter(request, response);
                        return;
                    } catch (Exception ex) {
                        log.error("Unable to refresh access token: {}", ex.getMessage());
                        response.sendRedirect("/app/?sessionExpired=true");
                        return;
                    }
                } else {
                    log.warn("No refresh token found, redirecting to login");
                    response.sendRedirect("/app/?sessionExpired=true");
                    return;
                }
            } catch (Exception e) {
                log.error("Invalid JWT token: {}", e.getMessage());
                response.sendRedirect("/app/?sessionExpired=true");
                return;
            }
        } else {
            log.warn("No JWT token found in request");
            response.sendRedirect("/app/?sessionExpired=true");
            return;
        }
        filterChain.doFilter(request, response);
    }


    private String refreshAccessToken(String refreshToken) {
        return authorizationWebClient.post()
                .uri("/refresh")
                .header("Authorization", "Bearer " + refreshToken)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
