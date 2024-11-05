package pl.kowalecki.dietplanner.controller.unlogged;

import jakarta.servlet.http.HttpServletResponse;

import lombok.AllArgsConstructor;
import org.springframework.http.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import pl.kowalecki.dietplanner.model.DTO.User.LoginRequestDTO;
import pl.kowalecki.dietplanner.services.loginService.UserLoginService;
import reactor.core.publisher.Mono;


@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RequestMapping("/app")
@Controller
@AllArgsConstructor
public class LoginController{
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
                    } else {
                        model.addAttribute("error", "Invalid email or password");
                        return Mono.just("pages/unlogged/index");
                    }
                })
                .onErrorResume(e -> {
                    System.out.println("Próbowałem się zalogować: " + loginRequestDto.getEmail());
                    model.addAttribute("error", "Bad data, try again");
                    return Mono.just("pages/unlogged/index");
                });
    }

}
