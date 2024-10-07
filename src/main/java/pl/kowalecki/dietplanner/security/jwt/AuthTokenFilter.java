package pl.kowalecki.dietplanner.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.kowalecki.dietplanner.services.WebPage.WebPageService;
import pl.kowalecki.dietplanner.model.DTO.User.UserDTO;
import pl.kowalecki.dietplanner.utils.ClassMapper;
import pl.kowalecki.dietplanner.utils.UrlTools;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private WebPageService webPageService;
    @Autowired
    private AuthJwtUtils jwtUtils;
    @Autowired
    private ClassMapper classMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/static/") || requestURI.startsWith("/resources/")) {
            filterChain.doFilter(request, response);
            return;
        }
        System.out.println("Metoda: " + request.getMethod() + " na adres: " + request.getRequestURI());
        String jwt = jwtUtils.getJwtFromCookies(request);
//        if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
//            handleAuthentication(request, response, jwt);
//        } else {
//            String refreshToken = jwtUtils.getJwtRefreshCookie(request);
//            if (refreshToken != null && jwtUtils.validateJwtToken(refreshToken)) {
//                ResponseEntity<String> refreshResponse = webPageService.sendRefreshJwtRequest(refreshToken, request, response);
//                String newJwt = refreshResponse.getBody();
//                if (newJwt != null) {
//                    //fixme bezpieczniej polecieć z tokenem
//                    response.addHeader(HttpHeaders.SET_COOKIE, "dietapp=" + newJwt);
//                    UserDTO userDetails = fetchUserDetailsFromApi(jwtUtils.getEmailFromJwtToken(newJwt), request, response);
//                    List<GrantedAuthority> authorities = userDetails.getRoles().stream()
//                            .map(SimpleGrantedAuthority::new)
//                            .collect(Collectors.toList());
//
//                    UsernamePasswordAuthenticationToken authentication =
//                            new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
//                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
//                }
//            }
//        }
        filterChain.doFilter(request, response);
    }


    private void handleAuthentication(HttpServletRequest request, HttpServletResponse response, String jwt) {
        String userEmail = jwtUtils.getEmailFromJwtToken(jwt);
        HttpSession session = request.getSession(false);
        UserDTO userDetails = null;

        if (session != null) {
            userDetails = classMapper.convertToDTO(session.getAttribute("user"), UserDTO.class);
        }

//        if (userDetails == null) {
//            userDetails = fetchUserDetailsFromApi(userEmail, request, response);
//            if (session != null && userDetails != null) {
//                session.setAttribute("user", userDetails);
//            }
//        }
        if (userDetails != null) {
            List<GrantedAuthority> authorities = userDetails.getRoles().stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            log.warn("User details could not be fetched or are invalid.");
        }
        //TODO logowanie później
//            webPageService.logUserAction(request);
    }

//    public UserDTO fetchUserDetailsFromApi(String userEmail, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
//        String url = "http://" + UrlTools.apiUrl + "/users/" + userEmail;
//        ResponseEntity<UserDTO> response = webPageService.sendGetRequest(url, UserDTO.class, httpRequest, httpResponse);
//
//        if (response.getStatusCode() == HttpStatus.OK) {
//            return response.getBody();
//        } else {
//            log.error("Failed to fetch user details for {}. Status: {}", userEmail, response.getStatusCode());
//            return null;
//        }
//    }
}
