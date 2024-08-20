package pl.kowalecki.dietplanner;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import pl.kowalecki.dietplanner.model.DTO.User.UserDTO;

public interface IWebPageService {
    boolean hasPermission();
    boolean validateToken(HttpServletRequest request);
    UserDetails getUserDetailsFromToken(HttpServletRequest request);
    void refreshUserSession(HttpServletRequest request);
    boolean isUserAuthorized(String requiredRole);
    void logUserAction(String username, String action);
    void setSessionAttribute(String key, Object value);
    Object getSessionAttribute(String key);
    void invalidateSession();
    <T> ResponseEntity<T> sendGetRequest(String url, Class<T> responseType);
    <T> ResponseEntity<T> sendPostRequest(String url, Object request, Class<T> responseType);
    boolean isUserLoggedIn();
    UserDTO getLoggedUser();
    void addCommonWebData(Model model);
}
