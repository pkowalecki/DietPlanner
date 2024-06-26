package pl.kowalecki.dietplanner.controller.unlogged;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import pl.kowalecki.dietplanner.controller.helper.RegisterControllerHelper;
import pl.kowalecki.dietplanner.exception.RegistrationException;
import pl.kowalecki.dietplanner.model.DTO.RegistrationRequestDTO;
import pl.kowalecki.dietplanner.repository.UserRepository;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@Controller
public class RegisterController {

    UserRepository userRepository;
    RegisterControllerHelper registerHelper;

    public RegisterController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(RegistrationRequestDTO registrationRequest, Model model, HttpSession session){

//        registerHelper.checkRegistrationData()
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);



//        if (!RegisterControllerHelper.checkUser(registrationRequest)){
//            throw new RegistrationException("Bad data");
//        }
        return ResponseEntity.ok("");
    }

    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<String> handleRegistrationException(RegistrationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

}
