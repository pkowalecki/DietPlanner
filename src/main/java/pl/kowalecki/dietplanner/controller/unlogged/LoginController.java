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
import reactor.core.publisher.Mono;


@RequestMapping("/app")
@Controller
@AllArgsConstructor
@Slf4j
public class LoginController {
    //    private final IWebPageService webPageService;
    private UserLoginService loginService;

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
                    if (loginResponse.getStatusCode() == HttpStatus.OK) {
                        loginResponse.getHeaders().get(HttpHeaders.SET_COOKIE)
                                .forEach(cookie -> httpResponse.addHeader(HttpHeaders.SET_COOKIE, cookie));
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
                    log.error("Próbowałem się zalogować: " + loginRequestDto.getEmail());
                    log.error("Błąd: " + e.getMessage());
                    model.addAttribute("error", "Unexpected error occurred. Please try again.");
                    return Mono.just("pages/unlogged/index");
                });
    }

}
