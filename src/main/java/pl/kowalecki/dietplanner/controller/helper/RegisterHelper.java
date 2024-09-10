package pl.kowalecki.dietplanner.controller.helper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.kowalecki.dietplanner.model.DTO.RegistrationRequestDTO;
import pl.kowalecki.dietplanner.services.UserDetails.UserDetailsServiceImpl;
import pl.kowalecki.dietplanner.utils.TextTools;

import java.util.*;

@Component
@AllArgsConstructor
public class RegisterHelper {

    private UserDetailsServiceImpl userService;


    static Map<String, String> errors;

    public Map<String, String> checkRegistrationData(RegistrationRequestDTO registrationRequest) {
        errors = new HashMap<>();
        checkNickname(registrationRequest.getNickname());
        checkUserName(registrationRequest.getName());
        checkSurname(registrationRequest.getSurname());
        checkEmail(registrationRequest.getEmailReg());
        checkPassword(registrationRequest.getPasswordReg(), registrationRequest.getPasswordReg2());
        return errors;
    }

    private void checkNickname(String nickname) {
        if (!TextTools.isTextLengthOk(nickname, 5, 50)) {
            errors.put(RegisterPole.NICKNAME.getFieldName(), "Length must be between 5-50");
        }
    }

    private void checkUserName(String userName) {
        if (!userName.equals("") && !TextTools.isTextLengthOk(userName, 5, 50)) {
            errors.put(RegisterPole.NAME.getFieldName(), "Length must be between 5-50");
        }
    }

    private void checkSurname(String surname) {
        if (!surname.equals("") && !TextTools.isTextLengthOk(surname, 5, 50)) {
            errors.put(RegisterPole.SURNAME.getFieldName(), "Length must be between 5-50");
        }
    }

    private void checkEmail(String email) {
        if (!TextTools.isTextLengthOk(email, 5, 50)) {
            errors.put(RegisterPole.EMAIL.getFieldName(), "Length must be between 5-50");;
        }
        //TODO TO ROBI API
//        if (userService.existsUserByEmail(email)) errors.put(RegisterPole.EMAIL.getFieldName(), "Email address already exists");
    }

    private void checkPassword(String password, String password2) {
        if (!password.equals(password2)) {
            errors.put(RegisterPole.PASSWORD2.getFieldName(), "Password not mach");
            return;
        }
        if (!TextTools.passwordPatternValidate(password))
            errors.put(RegisterPole.PASSWORD.getFieldName(), "Pattern not match");
    }

}
