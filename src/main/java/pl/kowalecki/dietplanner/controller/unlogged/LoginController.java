package pl.kowalecki.dietplanner.controller.unlogged;

import jakarta.servlet.http.HttpServletResponse;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import pl.kowalecki.dietplanner.model.DTO.User.LoginRequestDTO;
import pl.kowalecki.dietplanner.services.loginService.UserLoginService;
import pl.kowalecki.dietplanner.utils.AuthUtils;
import reactor.core.publisher.Mono;

import java.util.Map;


@RequestMapping("/app")
@Controller
@AllArgsConstructor
@Slf4j
public class LoginController {

    private final AuthUtils authUtils;
    private final UserLoginService loginService;


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(@RequestParam(value = "sessionExpired", required = false) String sessionExpired, Model model) {
        if ("true".equals(sessionExpired)) {
            model.addAttribute("sessionExpired", true);
        }
        return "pages/unlogged/index";
    }

    @PostMapping("/login")
    public Mono<String> postLoginPage(@ModelAttribute("loginForm") LoginRequestDTO loginRequestDto, Model model, HttpServletResponse httpResponse) {
        return loginService.postUserLogin(loginRequestDto)
                .flatMap(loginResponse -> {
                    if (loginResponse.getStatusCode() == HttpStatus.OK && loginResponse.getBody() != null) {

                        Map<String, String> tokens = (Map<String, String>) loginResponse.getBody();
                        String accessToken = tokens.get("accessToken");
                        String refreshToken = tokens.get("refreshToken");

                        log.info("Login successful for user: {}", loginRequestDto.getEmail());
                        log.debug("Access Token: {}, Refresh Token: {}", accessToken, refreshToken);

                        authUtils.setAccessTokenCookie(httpResponse, accessToken, 15 * 60);
                        authUtils.setRefreshTokenCookie(httpResponse, refreshToken, 7 * 24 * 60 * 60);
                        return Mono.just("redirect:/app/auth/loggedUserBoard");
                    } else if (loginResponse.getStatusCode() == HttpStatus.BAD_REQUEST) {
                        model.addAttribute("error", "Invalid email or password");
                        return Mono.just("pages/unlogged/index");
                    } else {
                        model.addAttribute("error", "Unexpected error occurred. Please try again.");
                        return Mono.just("pages/unlogged/index");
                    }
                })
                .onErrorResume(Exception.class, e -> {
                    log.error("Login attempt failed for email: {}", loginRequestDto.getEmail(), e);
                    model.addAttribute("error", "Unexpected error occurred. Please try again.");
                    return Mono.just("pages/unlogged/index");
                });
    }

}
