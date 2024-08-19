package pl.kowalecki.dietplanner.controller.logged;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.kowalecki.dietplanner.IWebPageService;

@Controller
@RequestMapping("/app/auth")
@AllArgsConstructor
public class LoggedBoardController {

    private final IWebPageService webPageService;

    @GetMapping("/loggedUserBoard")
    public String getLoggedUserBoard(Model model) {
        if (!webPageService.isUserLoggedIn()) {
            return "redirect:/app/";
        }
        model.addAttribute("user", webPageService.getLoggedUser());
        return "pages/logged/loggedPage";
    }
}
