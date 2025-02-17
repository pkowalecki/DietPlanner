package pl.kowalecki.dietplanner.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;


import java.util.Map;

@ControllerAdvice
@Slf4j
@AllArgsConstructor
public class GlobalControllerAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException e, Model model, HttpServletRequest request) {
        return handleError(e, model, request, "Nieprawidłowy parametr");
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception e, Model model, HttpServletRequest request) {
        return handleError(e, model, request, "Wystąpił nieoczekiwany błąd.");
    }

    private String handleError(Exception e, Model model, HttpServletRequest request, String userMessage) {
        Map<String, String[]> parameters = request.getParameterMap();
        String requestURI = request.getRequestURI();
        String errorMessage = userMessage + " (URL: " + requestURI + ")";
        if (!parameters.isEmpty()) {
            errorMessage += " Parametry: " + parameters.toString();
        }
        log.error(e.getMessage(), e);
        log.error(errorMessage);
        model.addAttribute("error", userMessage);
        model.addAttribute("logged", false);
        return "pages/unlogged/errorPage";
    }

    @ExceptionHandler(WebClientException.class)
    public String handleWebClientException(Exception e, Model model, HttpServletRequest request) {
        log.error("WebClient error at {}: {}", request.getRequestURI(), e.getMessage(), e);

        if (e instanceof WebClientResponseException.Unauthorized) {
            log.warn("Session expired or unauthorized access detected.");
            return "redirect:/?sessionExpired=true";
        }

        model.addAttribute("error", "Błąd podczas komunikacji z serwerem.");
        return "pages/unlogged/errorPage";
    }
}
