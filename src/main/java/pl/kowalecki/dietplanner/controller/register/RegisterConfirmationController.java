package pl.kowalecki.dietplanner.controller.register;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pl.kowalecki.dietplanner.service.register.IRegisterService;
import reactor.core.publisher.Mono;

@Controller
@AllArgsConstructor
public class RegisterConfirmationController {

    IRegisterService registerService;

    @GetMapping("/confirm")
    public Mono<String> confirmUser(@RequestParam("token") String confirmationToken, Model model) {
        return registerService.confirmUser(confirmationToken, model);
    }
}
