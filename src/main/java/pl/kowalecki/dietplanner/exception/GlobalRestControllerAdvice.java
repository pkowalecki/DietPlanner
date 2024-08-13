package pl.kowalecki.dietplanner.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.GregorianCalendar;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalRestControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> handleGenericException(Exception e, HttpServletRequest request) {
        Map<String, String[]> parameters = request.getParameterMap();
        String requestURI = request.getRequestURI();
        String errorMessage = "Wystąpił nieoczekiwany błąd przy wywołaniu " + requestURI;
        if (!parameters.isEmpty()) {
            errorMessage += " z parametrami: " + parameters.toString();
        }

        CustomErrorResponse errorResponse = new CustomErrorResponse("GENERAL_ERROR", "Wystąpił nieoczekiwany błąd.", new GregorianCalendar().getTimeInMillis());
        log.error(e.getMessage(), e);
        log.error(errorMessage);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }
}
