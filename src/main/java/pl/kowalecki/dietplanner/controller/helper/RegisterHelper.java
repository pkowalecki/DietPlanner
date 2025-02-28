package pl.kowalecki.dietplanner.controller.helper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.kowalecki.dietplanner.model.DTO.RegistrationRequestDTO;

import pl.kowalecki.dietplanner.utils.TextTools;

import java.util.*;

@Component
@AllArgsConstructor
public class RegisterHelper {

    public Map<String, String> checkRegistrationData(RegistrationRequestDTO registrationRequest) {
        Map<String, String> errors = new HashMap<>();
        checkNickname(registrationRequest.getNickname(), errors);
        checkUserName(registrationRequest.getName(), errors);
        checkSurname(registrationRequest.getSurname(), errors);
        checkEmail(registrationRequest.getEmailReg(), errors);
        checkPassword(registrationRequest.getPasswordReg(), registrationRequest.getPasswordReg2(), errors);
        return errors;
    }

    private void checkNickname(String nickname, Map<String, String> errors) {
        if (!TextTools.isTextLengthOk(nickname, 5, 50)) {
            errors.put(RegisterPole.NICKNAME.getFieldName(), "Długość musi być między 5 a 50 znaków");
        }
    }

    private void checkUserName(String userName, Map<String, String> errors) {
        if (!TextTools.isTextLengthOk(userName, 5, 50)) {
            errors.put(RegisterPole.NAME.getFieldName(), "Długość musi być między 5 a 50 znaków");
        }
    }

    private void checkSurname(String surname, Map<String, String> errors) {
        if (!TextTools.isTextLengthOk(surname, 5, 50)) {
            errors.put(RegisterPole.SURNAME.getFieldName(), "Długość musi być między 5 a 50 znaków");
        }
    }

    private void checkEmail(String email, Map<String, String> errors) {
        if (!TextTools.isTextLengthOk(email, 5, 50)) {
            errors.put(RegisterPole.EMAIL.getFieldName(), "Długość musi być między 5 a 50 znaków");
        }
    }

    private void checkPassword(String password, String password2, Map<String, String> errors) {
        if (password == null || password2 == null){
            errors.put(RegisterPole.PASSWORD.getFieldName(), "Hasło nie spełnia wymagań");
            return;
        }
        if (!password.equals(password2)) {
            errors.put(RegisterPole.PASSWORD2.getFieldName(), "Hasła nie są zgodne");
            return;
        }
        if (!TextTools.passwordPatternValidate(password)) {
            errors.put(RegisterPole.PASSWORD.getFieldName(), "Hasło nie spełnia wymagań");
        }
    }
}
