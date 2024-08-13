package pl.kowalecki.dietplanner.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
@Slf4j
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
        return "errorPage";
    }
}
