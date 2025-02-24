package pl.kowalecki.dietplanner.controller.register;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.kowalecki.dietplanner.exception.RegistrationException;
import pl.kowalecki.dietplanner.model.CustomResponse;
import pl.kowalecki.dietplanner.model.DTO.RegistrationRequestDTO;
import pl.kowalecki.dietplanner.service.register.IRegisterService;
import reactor.core.publisher.Mono;


@RequestMapping("/app")
@Controller
@AllArgsConstructor
public class RegisterController {

    IRegisterService registerService;

    @PostMapping("/register")
    public Mono<CustomResponse> registerUser(@RequestBody RegistrationRequestDTO registrationRequest) {
        return registerService.registerUser(registrationRequest);
    }

    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<String> handleRegistrationException(RegistrationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

}
