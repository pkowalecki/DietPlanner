package pl.kowalecki.dietplanner.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import pl.kowalecki.dietplanner.IWebPageService;
import pl.kowalecki.dietplanner.model.DTO.User.UserDTO;

import java.util.Map;

@ControllerAdvice
@Slf4j
@AllArgsConstructor
public class GlobalControllerAdvice {

    IWebPageService webPageService;

    @ModelAttribute
    public void addCommonWebData(Model model) {
        webPageService.addCommonWebData(model);
    }

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
        webPageService.addCommonWebData(model);
        return "pages/unlogged/errorPage";
    }
}
