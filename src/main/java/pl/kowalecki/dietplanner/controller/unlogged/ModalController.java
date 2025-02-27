package pl.kowalecki.dietplanner.controller.unlogged;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Controller
public class ModalController {

    @GetMapping("/registerModal")
    public Mono<String> getRegisterModal() {
        return Mono.just("pages/unlogged/registerModal");
    }
}