package pl.kowalecki.dietplanner.controller.helper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.kowalecki.dietplanner.model.DTO.RegistrationRequestDTO;

import pl.kowalecki.dietplanner.utils.TextTools;

import java.util.*;

@Component
@AllArgsConstructor
public class RegisterHelper {

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
            errors.put(RegisterPole.NICKNAME.getFieldName(), "Długość musi być między 5 a 50 znaków");
        }
    }

    private void checkUserName(String userName) {
        if (!userName.equals("") && !TextTools.isTextLengthOk(userName, 5, 50)) {
            errors.put(RegisterPole.NAME.getFieldName(), "Długość musi być między 5 a 50 znaków");
        }
    }

    private void checkSurname(String surname) {
        if (!surname.equals("") && !TextTools.isTextLengthOk(surname, 5, 50)) {
            errors.put(RegisterPole.SURNAME.getFieldName(), "Długość musi być między 5 a 50 znaków");
        }
    }

    private void checkEmail(String email) {
        if (!TextTools.isTextLengthOk(email, 5, 50)) {
            errors.put(RegisterPole.EMAIL.getFieldName(), "Długość musi być między 5 a 50 znaków");
        }
    }

    private void checkPassword(String password, String password2) {
        if (!password.equals(password2)) {
            errors.put(RegisterPole.PASSWORD2.getFieldName(), "Hasła nie są zgodne");
            return;
        }
        if (!TextTools.passwordPatternValidate(password))
            errors.put(RegisterPole.PASSWORD.getFieldName(), "Hasło nie spełnia wymagań");
    }

}
