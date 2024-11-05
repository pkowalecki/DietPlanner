package pl.kowalecki.dietplanner.controller.unlogged;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/app")
@AllArgsConstructor
public class CustomErrorController implements ErrorController {

//    @Autowired
//    IWebPageService webPageService;

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String errorMessage = "Wystąpił nieoczekiwany błąd";
        model.addAttribute("error", errorMessage);
//        webPageService.addCommonWebData(model);
        return "pages/unlogged/errorPage";
    }

}
