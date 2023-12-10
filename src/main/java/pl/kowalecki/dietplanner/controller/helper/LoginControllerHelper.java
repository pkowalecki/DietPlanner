package pl.kowalecki.dietplanner.controller.helper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import pl.kowalecki.dietplanner.model.AdministrationUser;
import pl.kowalecki.dietplanner.model.DTO.LoginRequestDTO;
import pl.kowalecki.dietplanner.model.Meal;
import pl.kowalecki.dietplanner.model.Role;
import pl.kowalecki.dietplanner.model.enums.EnumRole;
import pl.kowalecki.dietplanner.repository.AdministrationUserRepository;
import pl.kowalecki.dietplanner.repository.MealRepository;
import pl.kowalecki.dietplanner.repository.RoleRepository;
import pl.kowalecki.dietplanner.security.jwt.AuthJwtUtils;
import pl.kowalecki.dietplanner.security.services.AdministrationUserDetailsImpl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class LoginControllerHelper {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    AuthJwtUtils jwtUtils;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AdministrationUserRepository administrationUserRepository;

    @Autowired
    MealRepository mealRepository;



    public ResponseEntity<AdministrationUser> handleLoggedUserBoard(Model model, HttpSession session) {
        if (session.getAttribute("loggedUser") != null) {
                AdministrationUser user = (AdministrationUser) session.getAttribute("loggedUser");
                model.addAttribute("loggedUser", user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
