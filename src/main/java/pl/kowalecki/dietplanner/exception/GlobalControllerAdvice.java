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

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception e, Model model, HttpServletRequest request) {
        Map<String, String[]> parameters = request.getParameterMap();
        String requestURI = request.getRequestURI();
        String errorMessage = "Wystąpił nieoczekiwany błąd przy wywołaniu " + requestURI;
        if (!parameters.isEmpty()) {
            errorMessage += " z parametrami: " + parameters.toString();
        }
        log.error(e.getMessage(), e);
        log.error(errorMessage);
        model.addAttribute("error", "Wystąpił nieoczekiwany błąd.");
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
