package pl.kowalecki.dietplanner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pl.kowalecki.dietplanner.model.DTO.User.LoginResponseDTO;
import pl.kowalecki.dietplanner.model.DTO.User.UserDTO;
import pl.kowalecki.dietplanner.security.jwt.AuthJwtUtils;
import pl.kowalecki.dietplanner.services.UserDetailsImpl;
import pl.kowalecki.dietplanner.services.UserDetailsServiceImpl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@Slf4j
@AllArgsConstructor
public class WebPageService implements IWebPageService {

    @Autowired
    private AuthJwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private HttpSession session;

    @Autowired
    private final RestTemplate restTemplate;

    @Autowired
    private AuthJwtUtils authJwtUtils;

    @Override
    public boolean hasPermission() {
        return false;
    }

    @Override
    public boolean validateToken(HttpServletRequest request) {
        String token = jwtUtils.getJwtFromCookies(request);
        return token != null && jwtUtils.validateJwtToken(token);
    }

    @Override
    public UserDetails getUserDetailsFromToken(HttpServletRequest request) {
        String token = jwtUtils.getJwtFromCookies(request);
        if (token != null && jwtUtils.validateJwtToken(token)) {
            String email = jwtUtils.getEmailFromJwtToken(token);
            return userDetailsService.loadUserByUsername(email);
        }
        return null;
    }

    //TODO REFRESH SESSION
    @Override
    public void refreshUserSession(HttpServletRequest request) {
        UserDetails userDetails = getUserDetailsFromToken(request);
        if (userDetails != null) {
            log.info("Refreshed session for user: {}", ((UserDetailsImpl) userDetails).getEmail());
//            return authJwtUtils.generateJwtCookie(userDetails);

        }
    }

    @Override
    public boolean isUserAuthorized(String requiredRole) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getAuthorities() != null) {
            return authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(requiredRole));
        }
        return false;
    }


    @Override
    public void logUserAction(String username, String action) {

    }

    @Override
    public void setSessionAttribute(String key, Object value) {

    }

    @Override
    public Object getSessionAttribute(String key) {
        return null;
    }

    @Override
    public void invalidateSession() {

    }

    @Override
    public <T> ResponseEntity<T> sendGetRequest(String url, Class<T> responseType) {
        try {
            return restTemplate.getForEntity(url, responseType);
        } catch (HttpClientErrorException e) {
            HttpStatusCode status = e.getStatusCode();
            T body = null;
            try {
                body = new ObjectMapper().readValue(e.getResponseBodyAsString(), responseType);
            } catch (JsonProcessingException ex) {
                log.error("Error parsing error response: {}", ex.getMessage());
            }
            log.error("Error during GET request to {}: {}", url, e.getMessage());
            return new ResponseEntity<>(body, status);
        }
    }

    @Override
    public <T> ResponseEntity<T> sendPostRequest(String url, Object request, Class<T> responseType) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Object> entity = new HttpEntity<>(request, headers);
            return restTemplate.exchange(url, HttpMethod.POST, entity, responseType);
        }catch (Exception e){
            log.error("url: {}", url);
            log.error("request: {}", request);
            log.error("responseType: {}", responseType);
            log.error("message: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public boolean isUserLoggedIn() {
        return session.getAttribute("user") != null;
    }

    @Override
    public UserDTO getLoggedUser() {
//        UserDTO user = (UserDTO) session.getAttribute("user");
        LinkedHashMap<String, Object> userMap = (LinkedHashMap<String, Object>) session.getAttribute("user");
        if (userMap != null) {
            return buildUser(userMap);
        }
        return null;
    }

    private UserDTO buildUser(LinkedHashMap<String, Object> userMap) {
        LoginResponseDTO userRespo = new LoginResponseDTO();
        userRespo.setEmail((String) userMap.get("email"));
        UserDetailsImpl userDetails = (UserDetailsImpl)userDetailsService.loadUserByUsername(userRespo.getEmail());
       return UserDTO.builder()
                .id(userDetails.getId())
                .name(userDetails.getName())
                .surname(userDetails.getSurname())
                .email(userDetails.getEmail())
                .nickName(userDetails.getNickName())
                .roles(userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList())).build();
    }

    @Override
    public void addCommonWebData(Model model) {
        UserDTO user = getLoggedUser();
        if (user != null) {
//            session.setAttribute("user", user);
            model.addAttribute("user", user);
        }
    }
}
