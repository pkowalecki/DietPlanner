package pl.kowalecki.dietplanner.controller.logged;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.kowalecki.dietplanner.controller.helper.LoginControllerHelper;
import pl.kowalecki.dietplanner.model.User;

@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/auth")
@Controller
public class LoggedController {

    @Autowired
    LoginControllerHelper loginControllerHelper;

    @GetMapping("/loggedUserBoard")
    public ResponseEntity<User> getLoggedUserBoard(Model model, HttpSession session){
        return loginControllerHelper.handleLoggedUserBoard(model, session);
    }
}
