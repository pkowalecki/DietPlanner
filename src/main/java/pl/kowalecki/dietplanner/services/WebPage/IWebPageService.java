package pl.kowalecki.dietplanner.services.WebPage;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import pl.kowalecki.dietplanner.model.DTO.User.UserDTO;

public interface IWebPageService {
    boolean hasPermission();
    boolean validateToken(HttpServletRequest request);
    boolean isUserAuthorized(String requiredRole);
    void logUserAction(HttpServletRequest request);
    void setSessionAttribute(String key, Object value);
    Object getSessionAttribute(String key);
    void invalidateSession();
    ResponseEntity<String> sendRefreshJwtRequest(String refreshToken, HttpServletRequest httpRequest, HttpServletResponse httpResponse);
    <T> ResponseEntity<T> sendGetRequest(String url, Class<T> responseType, HttpServletRequest httpRequest, HttpServletResponse httpResponse);
    <T> ResponseEntity<T> sendPostRequest(String url, Object request, Class<T> responseType, HttpServletRequest httpRequest, HttpServletResponse httpResponse);
    UserDTO getLoggedUser();
    void addCommonWebData(Model model);
    void setMsg(MessageType type, String message);
}
