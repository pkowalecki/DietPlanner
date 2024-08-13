package pl.kowalecki.dietplanner.controller.unlogged;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/app")
public class ModalController {

    @GetMapping("/registerModal")
    public String getRegisterModal() {
        return "pages/unlogged/registerModal";
    }
}