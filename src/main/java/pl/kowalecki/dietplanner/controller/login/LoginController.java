package pl.kowalecki.dietplanner.controller.login;

import jakarta.servlet.http.HttpServletResponse;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.kowalecki.dietplanner.model.DTO.User.LoginRequest;
import pl.kowalecki.dietplanner.model.WebPageResponse;
import pl.kowalecki.dietplanner.service.login.ILoginService;
import reactor.core.publisher.Mono;


@Controller
@AllArgsConstructor
@Slf4j
public class LoginController {

    private final ILoginService loginService;

    @GetMapping("/")
    public Mono<String> index(@RequestParam(value = "sessionExpired", required = false) String sessionExpired, Model model) {
        if ("true".equals(sessionExpired)) {
            model.addAttribute("sessionExpired", true);
        }
        return Mono.just("pages/unlogged/index");
    }

    @PostMapping("/login")
    @ResponseBody
    public Mono<WebPageResponse> postLoginPage(@RequestBody LoginRequest loginRequest, Model model, HttpServletResponse httpResponse) {
        return loginService.login(loginRequest, model, httpResponse);
    }

}
