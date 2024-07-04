package pl.kowalecki.dietplanner.controller.unlogged;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.kowalecki.dietplanner.controller.helper.RegisterHelper;
import pl.kowalecki.dietplanner.exception.RegistrationException;
import pl.kowalecki.dietplanner.model.DTO.ErrorResponseDTO;
import pl.kowalecki.dietplanner.model.DTO.RegistrationRequestDTO;
import pl.kowalecki.dietplanner.repository.UserRepository;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@Controller
public class RegisterController {

    UserRepository userRepository;
    RegisterHelper registerHelper;

    @Autowired
    public RegisterController(UserRepository userRepository, RegisterHelper registerHelper) {
        this.userRepository = userRepository;
        this.registerHelper = registerHelper;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody RegistrationRequestDTO registrationRequest){
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> errors = registerHelper.checkRegistrationData(registrationRequest);
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorResponseDTO(errors));
        }
        // Implement user registration logic here
        return ResponseEntity.ok("Łi didit");
    }

    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<String> handleRegistrationException(RegistrationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

}
