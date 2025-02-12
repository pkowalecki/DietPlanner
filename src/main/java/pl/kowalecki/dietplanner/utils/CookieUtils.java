package pl.kowalecki.dietplanner.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import pl.kowalecki.dietplanner.exception.InvalidCookieException;

import java.util.Objects;

@Slf4j
@Component
public class CookieUtils {

    @Value("${web.cookie.accessToken.name}")
    private String accessTokenCookieName;

    @Value("${web.cookie.refreshToken.name}")
    private String refreshTokenCookieName;


    public void setAccessTokenCookie(HttpServletResponse response, String token, int maxAge) {
        setCookie(response, accessTokenCookieName, token, maxAge);
    }

    public void setRefreshTokenCookie(HttpServletResponse response, String token, int maxAge) {
        setCookie(response, refreshTokenCookieName, token, maxAge);
    }

    public String extractJwtokenFromAccessCookie(HttpServletRequest request) {
        return extractTokenFromRequest(request, accessTokenCookieName);
    }

    public String extractRefreshTokenFromRefreshCookie(HttpServletRequest request) {
        return extractTokenFromRequest(request, refreshTokenCookieName);
    }

    private void setCookie(HttpServletResponse response, String cookieName, String token, int maxAge) {
        Objects.requireNonNull(response, "HttpServletResponse must not be null");
        Objects.requireNonNull(cookieName, "CookieName must not be null");
        if (token == null || token.isBlank()) {
            throw new InvalidCookieException("Token cannot be null or empty");
        }

        if (maxAge < 0) {
            throw new InvalidCookieException("Max-Age cannot be negative");
        }
        ResponseCookie cookie = ResponseCookie.from(cookieName, token)
                .path("/")
                .maxAge(maxAge)
                .httpOnly(true)
                //.secure(true)
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private String extractTokenFromRequest(HttpServletRequest request, String cookieName) {
        Objects.requireNonNull(request, "HttpServletRequest must not be null");
        Objects.requireNonNull(cookieName, "CookieName must not be null");
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
