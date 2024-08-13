package pl.kowalecki.dietplanner.controller.logged;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/app/auth")
public class LoggedBoardController {

    @GetMapping("/loggedUserBoard")
    public String getLoggedUserBoard(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            return "redirect:/app/";
        }
        model.addAttribute("user", session.getAttribute("user"));
        return "pages/logged/loggedPage";
    }
}
