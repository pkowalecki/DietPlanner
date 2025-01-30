package pl.kowalecki.dietplanner.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import pl.kowalecki.dietplanner.services.loginService.AuthService;
import pl.kowalecki.dietplanner.utils.CookieUtils;
import pl.kowalecki.dietplanner.utils.RouteUtils;

import java.io.IOException;

@Slf4j
@Component
@AllArgsConstructor
public class CookieRequestFilter extends OncePerRequestFilter {

    private CookieUtils cookieUtils;
    private final RouteUtils routeUtils;
    private final AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (routeUtils.isOpenPath(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }
        String accessToken = cookieUtils.extractAccessCookie(request);
        String refreshToken = cookieUtils.extractRefreshCookie(request);

        if (accessToken == null && refreshToken != null) {
            String newAccessToken = attemptTokenRefresh(refreshToken);
            if (newAccessToken != null) {
                cookieUtils.setAccessTokenCookie(response, newAccessToken, 15 * 60);
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Session expired. Please log in again.");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private String attemptTokenRefresh(String refreshToken) {
        try {
            return authService.refreshAccessToken(refreshToken);
        } catch (WebClientResponseException e) {
            log.error("Error refreshing token, status: {}, message: {}", e.getStatusCode(), e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error refreshing token: {}", e.getMessage());
        }
        return null;
    }
}
