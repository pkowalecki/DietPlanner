package pl.kowalecki.dietplanner.controller.unlogged;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import pl.kowalecki.dietplanner.model.DTO.ConfirmationTokenDTO;
import pl.kowalecki.dietplanner.model.DTO.ResponseDTO;
import pl.kowalecki.dietplanner.services.UserServiceImpl;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RequestMapping("/app")
@RestController
@AllArgsConstructor
public class RegisterConfirmationController {

    private UserServiceImpl userService;

    @GetMapping(value = "/confirm")
    public ResponseEntity<ResponseDTO> confirmUser(WebRequest webRequest, @RequestParam("token") String confirmationToken){
        Map<String, String> errors = new HashMap<>();
        ConfirmationTokenDTO token = new ConfirmationTokenDTO(confirmationToken);
        Locale locale = webRequest.getLocale();
        System.out.println(locale);

        if (token.getToken() != null){
            boolean isActivated = userService.findAndActivateUserByHash(token.getToken());
            if (isActivated){
                return new ResponseEntity<>(HttpStatus.OK);
            }else {
                errors.put("ACTIVATED", "Account is already active");
                ResponseDTO response = ResponseDTO.builder()
                        .errors(errors)
                        .status(ResponseDTO.ResponseStatus.INVALID_TOKEN)
                        .build();
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }else {
            errors.put(ResponseDTO.ResponseStatus.INVALID_TOKEN.name(), "Invalid token");
            ResponseDTO response = ResponseDTO.builder()
                    .errors(errors)
                    .status(ResponseDTO.ResponseStatus.INVALID_TOKEN)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
