package pl.kowalecki.dietplanner.controller.logged;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.kowalecki.dietplanner.services.WebPage.IWebPageService;

@Controller
@RequestMapping("/app/auth")
@AllArgsConstructor
public class LoggedBoardController {

    private final IWebPageService webPageService;

    @GetMapping("/loggedUserBoard")
    public String getLoggedUserBoard(HttpSession session, HttpServletRequest request, Model model) {
        if (webPageService.getLoggedUser()!=null) {
            return "pages/logged/loggedPage";
        }
        return "redirect:/";
    }
}
