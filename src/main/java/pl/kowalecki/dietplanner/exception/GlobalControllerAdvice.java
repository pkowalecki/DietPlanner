package pl.kowalecki.dietplanner.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
@AllArgsConstructor
public class GlobalControllerAdvice {

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler({ClientErrorException.class, AccessDeniedException.class})
    public String handleClientErrorException(Exception e, Model model, HttpServletRequest request) {
        String errorMessage = extractErrorMessage(e);
        return handleError(e, model, request, errorMessage);
    }

    @ExceptionHandler({ IllegalArgumentException.class, Exception.class })
    public String handleGenericException(Exception e, Model model, HttpServletRequest request) {
        return handleError(e, model, request, "Wystąpił nieoczekiwany błąd.");
    }

    @ExceptionHandler(UnauthorizedException.class)
    public Object handleUnauthorizedException(UnauthorizedException e, HttpServletRequest request) {
        log.warn("Session expired or unauthorized access: {}", e.getMessage());
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("text/html")) {
            return "redirect:/?sessionExpired=true";
        }
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("type", "redirect");
        errorResponse.put("redirectUrl", "/?sessionExpired=true");
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


    private String extractErrorMessage(Exception e) {
        if (e instanceof AccessDeniedException) {
            return "Nie masz uprawnień do żądanego zasobu";
        }

        if (e instanceof ClientErrorException) {
            ClientErrorException clientError = (ClientErrorException) e;

            if (StringUtils.hasText(clientError.getMessage())) {
                return clientError.getMessage();
            }

            if (clientError.getStatusCode() == 403) {
                return "Nie masz uprawnień do żądanego zasobu";
            }
            if (clientError.getStatusCode() == 404) {
                return "Nie znaleziono żądanego zasobu";
            }
        }
        return "Wystąpił błąd podczas przetwarzania żądania";
    }
}
