package pl.kowalecki.dietplanner.restController;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.kowalecki.dietplanner.model.DTO.ConfirmationTokenDTO;
import pl.kowalecki.dietplanner.model.DTO.ResponseDTO;
import pl.kowalecki.dietplanner.services.UserServiceImpl;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/api")
@RestController
@AllArgsConstructor
public class RegisterConfirmationRestController {

    private UserServiceImpl userService;

    @GetMapping(value = "/confirm")
    public ResponseEntity<ResponseDTO> confirmUser(@RequestParam("token") String confirmationToken){
        Map<String, String> errors = new HashMap<>();
        ConfirmationTokenDTO token = new ConfirmationTokenDTO(confirmationToken);

        if (token.getToken() != null){
            boolean isActivated = userService.findAndActivateUserByHash(token.getToken());
            if (isActivated){
                return new ResponseEntity<>(HttpStatus.OK);
            }else {
                errors.put("ACTIVATED", "Account is already active");
                ResponseDTO response = ResponseDTO.builder()
                        .status(ResponseDTO.ResponseStatus.ERROR)
                        .errors(errors)
                        .build();
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }else {
            errors.put("INVALID_TOKEN", "Invalid token");
            ResponseDTO response = ResponseDTO.builder()
                    .status(ResponseDTO.ResponseStatus.ERROR)
                    .errors(errors)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }
}
