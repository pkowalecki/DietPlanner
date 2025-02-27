package pl.kowalecki.dietplanner.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
@AllArgsConstructor
public class GlobalControllerAdvice {

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler({ IllegalArgumentException.class, Exception.class })
    public String handleGenericException(Exception e, Model model, HttpServletRequest request) {
        return handleError(e, model, request, "Wystąpił nieoczekiwany błąd.");
    }

    @ExceptionHandler(UnauthorizedException.class)
    public Object handleUnauthorizedException(UnauthorizedException e, HttpServletRequest request) {
        log.warn("Session expired or unauthorized access: {}", e.getMessage());
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("text/html")) {
            return "redirect:/app/login?sessionExpired=true";
        }
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("type", "redirect");
        errorResponse.put("redirectUrl", "/app/login?sessionExpired=true");
        errorResponse.put("message", "Sesja wygasła. Proszę się ponownie zalogować.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    private String handleError(Exception e, Model model, HttpServletRequest request, String userMessage) {
        String timestamp = LocalDateTime.now().format(dtf);
        String requestURI = request.getRequestURI();
        String errorMessage = String.format("[%s] %s (URL: %s)", timestamp, userMessage, requestURI);
        Map<String, String[]> parameters = request.getParameterMap();
        if (!parameters.isEmpty()) {
            errorMessage += " Parametry: " + parameters.toString();
        }
        log.error(errorMessage, e);
        model.addAttribute("error", userMessage);
        model.addAttribute("logged", false);
        return "pages/unlogged/errorPage";
    }
}
