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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.kowalecki.dietplanner.WebPageService;
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
        System.out.println("Metoda: " + request.getMethod() + " na adres: " + request.getRequestURI());
        if (requestURI.startsWith("/static/") || requestURI.startsWith("/resources/")) {
            filterChain.doFilter(request, response);
            return;
        }
        String jwt = jwtUtils.getJwtFromCookies(request);
        if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
            String userEmail = jwtUtils.getEmailFromJwtToken(jwt);

            HttpSession session = request.getSession(false);
            UserDTO userDetails = null;
            if (session != null) {
                userDetails = classMapper.convertToDTO(session.getAttribute("user"), UserDTO.class);
            }

            if (userDetails == null) {
                userDetails = fetchUserDetailsFromApi(userEmail, request, response);

                if (session != null && userDetails != null) {
                    session.setAttribute("user", userDetails);
                }
            }
            List<GrantedAuthority> authorities = userDetails.getRoles().stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            //TODO logowanie później
//            webPageService.logUserAction(request);
        }
        filterChain.doFilter(request, response);
    }

    public UserDTO fetchUserDetailsFromApi(String userEmail, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        String url = "http://"+ UrlTools.apiUrl+"/users/" + userEmail;
        ResponseEntity<UserDTO> response = webPageService.sendGetRequest(url, UserDTO.class, httpRequest, httpResponse);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            log.error("Failed to fetch user details for {}. Status: {}", userEmail, response.getStatusCode());
            return null;
        }
    }
}
