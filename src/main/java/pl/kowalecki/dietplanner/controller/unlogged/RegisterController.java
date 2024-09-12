package pl.kowalecki.dietplanner.controller.unlogged;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.kowalecki.dietplanner.controller.helper.RegisterHelper;
import pl.kowalecki.dietplanner.exception.RegistrationException;
import pl.kowalecki.dietplanner.model.DTO.ResponseBodyDTO;
import pl.kowalecki.dietplanner.model.DTO.RegistrationRequestDTO;

import java.util.*;

@RequestMapping("/app")
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@Controller
@AllArgsConstructor
public class RegisterController {

    RegisterHelper registerHelper;



    @PostMapping("/register")
    public ResponseEntity<ResponseBodyDTO> registerUser(@RequestBody RegistrationRequestDTO registrationRequest) {
        Map<String, String> errors = registerHelper.checkRegistrationData(registrationRequest);
        ResponseBodyDTO response;
        if (!errors.isEmpty()) {
            response = ResponseBodyDTO.builder()
                    .status(ResponseBodyDTO.ResponseStatus.BAD_DATA)
                    .data(errors)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        //FIXME to API tworzy nam usera i śle maila
//        User user = userServiceImpl.createUser(registrationRequest);
//        try {
//            user.setRoles(userServiceImpl.setUserRoles(Collections.singletonList("ROLE_USER")));
//        }catch (RegistrationException e){
//            errors.put(RegisterPole.ROLE.getFieldName(), "Role error, contact administration");
//             response = ResponseDTO.builder()
//                    .status(ResponseDTO.ResponseStatus.BADDATA)
//                    .data(errors)
//                    .build();
//            return new ResponseEntity<>(response, HttpStatus.OK);
//        }
//
//        userServiceImpl.registerUser(user);
//        //Jedziemy z mailerem gmail nie czyta html
//        boolean isHtml = !user.getEmail().contains("@gmail.com");
//        mailerService.sendRegistrationEmail(user.getEmail(), user.getHash(), isHtml);

        response = ResponseBodyDTO.builder()
                .status(ResponseBodyDTO.ResponseStatus.OK)
                .message("Registration successful")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }




    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<String> handleRegistrationException(RegistrationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

}
