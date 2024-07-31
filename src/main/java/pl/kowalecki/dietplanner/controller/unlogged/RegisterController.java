package pl.kowalecki.dietplanner.controller.unlogged;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.kowalecki.dietplanner.controller.helper.RegisterHelper;
import pl.kowalecki.dietplanner.controller.helper.RegisterPole;
import pl.kowalecki.dietplanner.exception.RegistrationException;
import pl.kowalecki.dietplanner.model.DTO.RegisterResponseDTO;
import pl.kowalecki.dietplanner.model.DTO.RegistrationRequestDTO;
import pl.kowalecki.dietplanner.model.User;
import pl.kowalecki.dietplanner.services.UserService;

import java.util.*;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@Controller
public class RegisterController {

    UserService userService;
    RegisterHelper registerHelper;

    @Autowired
    public RegisterController(UserService userService, RegisterHelper registerHelper) {
        this.userService = userService;
        this.registerHelper = registerHelper;
    }


    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> registerUser(@Valid @RequestBody RegistrationRequestDTO registrationRequest) {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        Map<String, String> errors = registerHelper.checkRegistrationData(registrationRequest);
        if (!errors.isEmpty()) {
            RegisterResponseDTO response = RegisterResponseDTO.builder().status(RegisterResponseDTO.RegisterStatus.BADDATA).errors(errors).build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        User user = userService.createUser(registrationRequest);
        try {
            user.setRoles(userService.setUserRoles(Collections.singletonList("ROLE_USER")));
        }catch (RegistrationException e){
            errors.put(RegisterPole.ROLE.getFieldName(), "Role error, contact administration");
            RegisterResponseDTO response = RegisterResponseDTO.builder()
                    .status(RegisterResponseDTO.RegisterStatus.BADDATA)
                    .errors(errors)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        userService.registerUser(user);

        //Jedziemy z mailerem


        return ResponseEntity.ok(new RegisterResponseDTO(RegisterResponseDTO.RegisterStatus.OK));

    }




    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<String> handleRegistrationException(RegistrationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

}
