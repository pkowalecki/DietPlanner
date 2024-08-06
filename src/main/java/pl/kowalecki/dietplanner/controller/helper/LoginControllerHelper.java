package pl.kowalecki.dietplanner.controller.helper;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import pl.kowalecki.dietplanner.model.User;
import pl.kowalecki.dietplanner.repository.RoleRepository;
import pl.kowalecki.dietplanner.security.jwt.AuthJwtUtils;
import pl.kowalecki.dietplanner.services.UserServiceImpl;

@Component
@AllArgsConstructor
public class LoginControllerHelper {

    AuthenticationManager authenticationManager;
    AuthJwtUtils jwtUtils;
    PasswordEncoder encoder;
    RoleRepository roleRepository;
    UserServiceImpl userService;



    public ResponseEntity<User> handleLoggedUserBoard(Model model, HttpSession session) {
        if (session.getAttribute("loggedUser") != null) {
                User user = (User) session.getAttribute("loggedUser");
                model.addAttribute("loggedUser", user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
