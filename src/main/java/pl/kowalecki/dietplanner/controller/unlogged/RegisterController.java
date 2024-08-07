package pl.kowalecki.dietplanner.controller.unlogged;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kowalecki.dietplanner.controller.helper.RegisterHelper;
import pl.kowalecki.dietplanner.controller.helper.RegisterPole;
import pl.kowalecki.dietplanner.exception.RegistrationException;
import pl.kowalecki.dietplanner.mailService.MailerService;
import pl.kowalecki.dietplanner.model.DTO.ResponseDTO;
import pl.kowalecki.dietplanner.model.DTO.RegistrationRequestDTO;
import pl.kowalecki.dietplanner.model.User;
import pl.kowalecki.dietplanner.services.UserServiceImpl;

import java.util.*;

@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
public class RegisterController {

    UserServiceImpl userServiceImpl;
    RegisterHelper registerHelper;
    MailerService mailerService;

    @Autowired
    public RegisterController(UserServiceImpl userServiceImpl, RegisterHelper registerHelper, MailerService mailerService) {
        this.userServiceImpl = userServiceImpl;
        this.registerHelper = registerHelper;
        this.mailerService = mailerService;
    }


    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> registerUser(@Valid @RequestBody RegistrationRequestDTO registrationRequest) {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        Map<String, String> errors = registerHelper.checkRegistrationData(registrationRequest);
        if (!errors.isEmpty()) {
            ResponseDTO response = ResponseDTO.builder().status(ResponseDTO.ResponseStatus.BADDATA).errors(errors).build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        User user = userServiceImpl.createUser(registrationRequest);
        try {
            user.setRoles(userServiceImpl.setUserRoles(Collections.singletonList("ROLE_USER")));
        }catch (RegistrationException e){
            errors.put(RegisterPole.ROLE.getFieldName(), "Role error, contact administration");
            ResponseDTO response = ResponseDTO.builder()
                    .status(ResponseDTO.ResponseStatus.BADDATA)
                    .errors(errors)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        userServiceImpl.registerUser(user);
        //Jedziemy z mailerem gmail nie czyta html
        boolean isHtml = !user.getEmail().contains("@gmail.com");
        mailerService.sendRegistrationEmail(user.getEmail(), user.getHash(), isHtml);

        return ResponseEntity.ok(new ResponseDTO(ResponseDTO.ResponseStatus.OK));

    }




    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<String> handleRegistrationException(RegistrationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

}
