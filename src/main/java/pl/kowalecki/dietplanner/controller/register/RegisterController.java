package pl.kowalecki.dietplanner.controller.register;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.kowalecki.dietplanner.model.WebPageResponse;
import pl.kowalecki.dietplanner.model.DTO.RegistrationRequestDTO;
import pl.kowalecki.dietplanner.service.register.IRegisterService;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
public class RegisterController {

    IRegisterService registerService;

    @PostMapping(value = "/register")
    public Mono<WebPageResponse> registerUser(@RequestBody RegistrationRequestDTO registrationRequest) {
        return registerService.registerUser(registrationRequest);
    }
}
