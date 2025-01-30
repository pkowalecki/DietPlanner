package pl.kowalecki.dietplanner.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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

    public String extractAccessCookie(HttpServletRequest request) {
        return extractTokenFromRequest(request, accessTokenCookieName);
    }

    public String extractRefreshCookie(HttpServletRequest request) {
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
}
