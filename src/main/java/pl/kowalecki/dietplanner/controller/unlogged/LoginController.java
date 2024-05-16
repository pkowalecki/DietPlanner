package pl.kowalecki.dietplanner.controller.unlogged;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import pl.kowalecki.dietplanner.controller.helper.LoginControllerHelper;
import pl.kowalecki.dietplanner.model.User;
import pl.kowalecki.dietplanner.model.DTO.LoginRequestDTO;
import pl.kowalecki.dietplanner.model.Role;
import pl.kowalecki.dietplanner.model.enums.EnumRole;
import pl.kowalecki.dietplanner.repository.UserRepository;
import pl.kowalecki.dietplanner.repository.MealRepository;
import pl.kowalecki.dietplanner.repository.RoleRepository;
import pl.kowalecki.dietplanner.security.jwt.AuthJwtUtils;
import pl.kowalecki.dietplanner.security.services.UserDetailsImpl;

import java.util.Set;
import java.util.stream.Collectors;


@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@Controller
public class LoginController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    AuthJwtUtils jwtUtils;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MealRepository mealRepository;
    @Autowired
    private LoginControllerHelper loginControllerHelper;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(){
        return "pages/unlogged/index";
    }

    @PostMapping("/login")
    public String postLoginPage(@ModelAttribute("loginForm") LoginRequestDTO loginRequestDto, HttpServletResponse response, HttpSession session, Model model){
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            Set<Role> roles = userDetails.getAuthorities().stream()
                    .map(authority -> {
                        String roleName = authority.getAuthority();
                        EnumRole enumRole = EnumRole.valueOf(roleName);
                        Role role = new Role();
                        role.setName(enumRole);
                        return role;
                    })
                    .collect(Collectors.toSet());

            ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

            response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());
            session.setAttribute("user", new User(userDetails.getId(),
                    userDetails.getName(),
                    userDetails.getNickName(),
                    userDetails.getSurname(),
                    userDetails.getEmail(),
                    roles));
            return "pages/foodBoardPage";

        } catch (AuthenticationException e) {
            System.out.println("Próbowałem się zalogować: " + loginRequestDto.getEmail());
            model.addAttribute("error", "Bad data, try again");
            return "pages/unlogged/index";
        }

    }

}
