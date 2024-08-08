package pl.kowalecki.dietplanner.restController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.kowalecki.dietplanner.model.DTO.LoginRequestDTO;
import pl.kowalecki.dietplanner.model.DTO.LoginResponseDTO;
import pl.kowalecki.dietplanner.model.DTO.ResponseDTO;
import pl.kowalecki.dietplanner.model.User;
import pl.kowalecki.dietplanner.security.jwt.AuthJwtUtils;
import pl.kowalecki.dietplanner.services.UserDetailsImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Slf4j
public class AuthRestController {

    AuthenticationManager authenticationManager;
    AuthJwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@ModelAttribute("loginForm") LoginRequestDTO loginRequestDto, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
            response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

            Map<String, LoginResponseDTO> userDetailsResponse = new HashMap<>();
            userDetailsResponse.put("user", LoginResponseDTO.builder()
                    .email(userDetails.getEmail())
                    .roles(userDetails.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList()))
                    .build());
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .body(new ResponseDTO(ResponseDTO.ResponseStatus.OK, "Login successful", userDetailsResponse));


        } catch (AuthenticationException e) {
            Map<String, String> errors = new HashMap<>();
            errors.put("error", "Invalid email or password");
            return new ResponseEntity<>(new ResponseDTO(ResponseDTO.ResponseStatus.ERROR, "Authentication error", errors), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        ResponseCookie cleanCookie = jwtUtils.getCleanJwtCookie();
        response.addHeader(HttpHeaders.SET_COOKIE, cleanCookie.toString());
        return ResponseEntity.ok().body(new ResponseDTO(ResponseDTO.ResponseStatus.OK));
    }
}
