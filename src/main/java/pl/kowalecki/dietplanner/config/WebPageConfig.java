package pl.kowalecki.dietplanner.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import pl.kowalecki.dietplanner.services.WebPage.IWebPageService;

import java.io.IOException;

@ControllerAdvice
@Slf4j
public class WebPageConfig {

    @Autowired
    IWebPageService webPageService;

    @ModelAttribute
    public void insertPageAttributes(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //Insertuje wszystkie dane które page wymaga.
        try{
            System.out.println("Putuje dane do pejdża");
            model.addAttribute("errors", webPageService.getErrors());
            model.addAttribute("errorsList", webPageService.getErrorsAsList());

        } catch (Throwable e) {
            log.error(e.getMessage());
            e.printStackTrace();
            try {
                response.sendRedirect("/app/error");
            }catch (Exception e1) {
                log.error("Failed to redirect to error page: ", e1);
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Critical error occurred.");
            }
        }

    }
}
