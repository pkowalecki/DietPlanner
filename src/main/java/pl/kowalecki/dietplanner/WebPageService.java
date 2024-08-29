package pl.kowalecki.dietplanner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pl.kowalecki.dietplanner.model.DTO.User.LoginResponseDTO;
import pl.kowalecki.dietplanner.model.DTO.User.UserDTO;
import pl.kowalecki.dietplanner.security.jwt.AuthJwtUtils;
import pl.kowalecki.dietplanner.utils.ClassMapper;

import java.io.IOException;
import java.util.List;

@Configuration
@Slf4j
@AllArgsConstructor
public class WebPageService implements IWebPageService {

    @Autowired
    private AuthJwtUtils jwtUtils;

    @Autowired
    private HttpSession session;

    @Autowired
    private final RestTemplate restTemplate;

    @Autowired
    private AuthJwtUtils authJwtUtils;

    @Autowired
    ClassMapper classMapper;

    @Override
    public boolean hasPermission() {
        return false;
    }

    @Override
    public boolean validateToken(HttpServletRequest request) {
        String token = jwtUtils.getJwtFromCookies(request);
        return token != null && jwtUtils.validateJwtToken(token);
    }

    //TODO REFRESH SESSION
    @Override
    public void refreshUserSession(HttpServletRequest request) {
//        UserDetails userDetails = getUserDetailsFromToken(request);
//        if (userDetails != null) {
//            log.info("Refreshed session for user: {}", ((UserDetailsImpl) userDetails).getEmail());
////            return authJwtUtils.generateJwtCookie(userDetails);
//
//        }
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

    public <T> ResponseEntity<T> sendGetRequest(String url, Class<T> responseType, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        return sendRequest(url, HttpMethod.GET, null, responseType, httpRequest, httpResponse);
    }

    public <T> ResponseEntity<T> sendPostRequest(String url, Object request, Class<T> responseType, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        return sendRequest(url, HttpMethod.POST, request, responseType, httpRequest, httpResponse);
    }

    private <T> ResponseEntity<T> sendRequest(String url, HttpMethod method, Object request, Class<T> responseType, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String token = jwtUtils.getJwtFromCookies(httpRequest);
            if (token != null) {
                headers.add(HttpHeaders.COOKIE, "dietapp=" + token);
            }

            HttpEntity<Object> entity = new HttpEntity<>(request, headers);
            ResponseEntity<String> response = restTemplate.exchange(url, method, entity, String.class);

            List<String> cookies = response.getHeaders().get(HttpHeaders.SET_COOKIE);
            if (cookies != null) {
                for (String cookieHeader : cookies) {
                    httpResponse.addHeader(HttpHeaders.SET_COOKIE, cookieHeader);
                }
            }

            log.info("Received response from {}: {}", url, response.getBody());
            if (response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON)) {
                T body = new ObjectMapper().readValue(response.getBody(), responseType);
                return new ResponseEntity<>(body, response.getStatusCode());
            } else if (response.getHeaders().getContentType().includes(MediaType.TEXT_PLAIN)) {
                T body = (T) response.getBody();
                return new ResponseEntity<>(body, response.getStatusCode());
            } else {
                log.error("Unexpected content type: {}", response.getHeaders().getContentType());
                throw new HttpClientErrorException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Unsupported Media Type");
            }
        } catch (HttpClientErrorException e) {
            HttpStatusCode status = e.getStatusCode();
            T body = null;
            try {
                body = new ObjectMapper().readValue(e.getResponseBodyAsString(), responseType);
            } catch (JsonProcessingException ex) {
                log.error("Error parsing error response: {}", ex.getMessage());
            }
            log.error("Error during request to {}: {}", url, e.getMessage());
            return new ResponseEntity<>(body, status);
        } catch (IOException e) {
            log.error("Error processing response: {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public boolean isUserLoggedIn() {
        return session.getAttribute("user") != null;
    }

    @Override
    public UserDTO getLoggedUser() {
       LoginResponseDTO loginResponseDTO = classMapper.convertToDTO(session.getAttribute("user"), LoginResponseDTO.class);
        if (loginResponseDTO != null) {
            return UserDTO.builder()
                    .id(loginResponseDTO.getId())
                    .surname(loginResponseDTO.getSurname())
                    .nickName(loginResponseDTO.getNickName())
                    .name(loginResponseDTO.getName())
                    .email(loginResponseDTO.getEmail())
                    .roles(loginResponseDTO.getRoles())
                    .build();
        }
        return null;
    }

    @Override
    public void addCommonWebData(Model model) {
        UserDTO user = getLoggedUser();
        if (user != null) {
            model.addAttribute("user", user);
        }
    }
}
