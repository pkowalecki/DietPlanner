package pl.kowalecki.dietplanner.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;
import pl.kowalecki.dietplanner.services.UserDetailsImpl;

import java.security.Key;
import java.util.Date;

@Component
@Slf4j
public class AuthJwtUtils {

    @Value("${dietplanner.app.jwtSecret}")
    private String jwtSecret;

    @Value("${dietplanner.app.jwtExpiration}")
    private int jwtExpiration;

    @Value("${dietplanner.app.jwtCookieName}")
    private String jwtCookie;

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        return cookie != null ? cookie.getValue() : null;
    }

    private String generateTokenFromEmail(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpiration))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }


    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
        String jwt = generateTokenFromEmail(userPrincipal.getEmail());
        return ResponseCookie.from(jwtCookie, jwt)
                .path("/")
                .maxAge(12 * 60 * 60)
                .httpOnly(true)
                .build();
    }

    public ResponseCookie getCleanJwtCookie() {
        return ResponseCookie
                .from(jwtCookie, null)
                .maxAge(0)
                .path("/")
                .build();
    }

    public String getEmailFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
