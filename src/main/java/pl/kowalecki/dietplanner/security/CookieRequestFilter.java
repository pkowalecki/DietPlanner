package pl.kowalecki.dietplanner.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.kowalecki.dietplanner.utils.CookieUtils;
import pl.kowalecki.dietplanner.utils.RouteUtils;

import java.io.IOException;

@Slf4j
@Component
@AllArgsConstructor
public class CookieRequestFilter extends OncePerRequestFilter {

    private final CookieUtils cookieUtils;
    private final RouteUtils routeUtils;;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (routeUtils.isOpenPath(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }
        String accessToken = cookieUtils.extractJwtokenFromAccessCookie(request);
        String refreshToken = cookieUtils.extractRefreshTokenFromRefreshCookie(request);

        if (accessToken == null && refreshToken == null) {
            //FIXME Zrobić poprawny redir na brak sesji
            response.sendRedirect("/app/login?sessionExpired=true");
            return;
        }
        filterChain.doFilter(request, response);
    }
}
