package pl.kowalecki.dietplanner.controller.unlogged;

import jakarta.servlet.http.Cookie;
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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import pl.kowalecki.dietplanner.controller.helper.LoginControllerHelper;
import pl.kowalecki.dietplanner.model.AdministrationUser;
import pl.kowalecki.dietplanner.model.DTO.LoginRequestDTO;
import pl.kowalecki.dietplanner.model.Role;
import pl.kowalecki.dietplanner.model.enums.EnumRole;
import pl.kowalecki.dietplanner.repository.AdministrationUserRepository;
import pl.kowalecki.dietplanner.repository.MealRepository;
import pl.kowalecki.dietplanner.repository.RoleRepository;
import pl.kowalecki.dietplanner.security.jwt.AuthJwtUtils;
import pl.kowalecki.dietplanner.security.services.AdministrationUserDetailsImpl;

import java.util.Set;
import java.util.stream.Collectors;


@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RequestMapping("/public")
@RestController
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
    AdministrationUserRepository administrationUserRepository;

    @Autowired
    MealRepository mealRepository;
    @Autowired
    private LoginControllerHelper loginControllerHelper;
    @PostMapping("/login")
    public ResponseEntity<?> postLoginPage(@RequestBody LoginRequestDTO loginRequestDto, HttpServletResponse response, HttpSession session){
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            AdministrationUserDetailsImpl userDetails = (AdministrationUserDetailsImpl) authentication.getPrincipal();

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
            return new ResponseEntity<>(new AdministrationUser(userDetails.getId(),
                    userDetails.getName(),
                    userDetails.getSurname(),
                    userDetails.getEmail(),
                    roles), HttpStatus.OK);

        } catch (AuthenticationException e) {
            System.out.println("Próbowałem się zalogować: " + loginRequestDto.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }


}
