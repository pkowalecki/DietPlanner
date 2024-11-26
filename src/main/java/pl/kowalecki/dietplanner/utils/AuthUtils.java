package pl.kowalecki.dietplanner.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.security.Key;

@Slf4j
@Component
public class AuthUtils {

    @Value("${web.cookie.accessToken.name}")
    private String accessTokenCookieName;

    @Value("${web.cookie.refreshToken.name}")
    private String refreshTokenCookieName;

    @Value("${web.app.jwtSecret}")
    private String jwtSecret;

    public void validateToken(final String token) {
        Jwts.parserBuilder().setSigningKey(key()).build().parse(token);
    }

    public void setAccessTokenCookie(HttpServletResponse response, String token, int maxAge) {
        setCookie(response, accessTokenCookieName, token, maxAge);
    }

    public void setRefreshTokenCookie(HttpServletResponse response, String token, int maxAge) {
        setCookie(response, refreshTokenCookieName, token, maxAge);
    }

    public String extractAccessTokenFromRequest(HttpServletRequest request) {
        return extractTokenFromRequest(request, accessTokenCookieName);
    }

    public String extractRefreshTokenFromRequest(HttpServletRequest request) {
        return extractTokenFromRequest(request, refreshTokenCookieName);
    }

    private void setCookie(HttpServletResponse response, String cookieName, String token, int maxAge) {
        ResponseCookie cookie = ResponseCookie.from(cookieName, token)
                .path("/")
                .maxAge(maxAge)
                .httpOnly(true)
                //.secure(true)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private String extractTokenFromRequest(HttpServletRequest request, String cookieName) {
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

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("Failed to parse JWT: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid token", e);
        }
    }
}
