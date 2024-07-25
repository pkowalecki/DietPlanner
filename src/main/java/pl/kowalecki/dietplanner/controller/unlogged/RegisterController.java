package pl.kowalecki.dietplanner.controller.unlogged;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.kowalecki.dietplanner.controller.helper.RegisterHelper;
import pl.kowalecki.dietplanner.controller.helper.RegisterPole;
import pl.kowalecki.dietplanner.exception.RegistrationException;
import pl.kowalecki.dietplanner.model.DTO.RegisterResponseDTO;
import pl.kowalecki.dietplanner.model.DTO.RegistrationRequestDTO;
import pl.kowalecki.dietplanner.model.Role;
import pl.kowalecki.dietplanner.model.User;
import pl.kowalecki.dietplanner.model.enums.EnumRole;
import pl.kowalecki.dietplanner.repository.RoleRepository;
import pl.kowalecki.dietplanner.repository.UserRepository;

import java.util.*;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@Controller
public class RegisterController {

    UserRepository userRepository;
    RegisterHelper registerHelper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    @Autowired
    public RegisterController(UserRepository userRepository, RegisterHelper registerHelper, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.registerHelper = registerHelper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }


    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> registerUser(@RequestBody RegistrationRequestDTO registrationRequest) {
        Map<String, String> errors;
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        errors = registerHelper.checkRegistrationData(registrationRequest);
        User user = createUser(registrationRequest);
        user.setRoles(registerHelper.setUserRole(Collections.singletonList("ROLE_USER")));
        if (user.getRoles() == null || user.getRoles().size() == 0) {
            errors.put(RegisterPole.ROLE.getFieldName(), "Role error");
        }
        if (!errors.isEmpty()) {
            RegisterResponseDTO respo = RegisterResponseDTO.builder().status(RegisterResponseDTO.RegisterStatus.BADDATA).errors(errors).build();
            return new ResponseEntity<>(respo, HttpStatus.OK);
        }
        userRepository.save(user);

        //Jedziemy z mailerem

        return ResponseEntity.ok(new RegisterResponseDTO(RegisterResponseDTO.RegisterStatus.OK));

    }


    private User createUser(RegistrationRequestDTO registrationRequest) {
        return new User(
                registrationRequest.getName() != null ? registrationRequest.getName() : "",
                registrationRequest.getSurname() != null ? registrationRequest.getSurname() : "",
                registrationRequest.getEmailReg(),
                registrationRequest.getNickname(),
                passwordEncoder.encode(registrationRequest.getPasswordReg()));
    }

    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<String> handleRegistrationException(RegistrationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

}
