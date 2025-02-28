package pl.kowalecki.dietplanner.controller.helper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.kowalecki.dietplanner.model.DTO.RegistrationRequestDTO;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RegisterHelperTest {

    RegisterHelper registerHelper;
    RegistrationRequestDTO registrationRequestDTO;

    @BeforeEach
    void setUp() {
        registerHelper = new RegisterHelper();
        registrationRequestDTO = new RegistrationRequestDTO();
        registrationRequestDTO.setNickname("aaaaa");
        registrationRequestDTO.setName("aaaaa");
        registrationRequestDTO.setSurname("aaaaa");
        registrationRequestDTO.setEmailReg("test@test.pl");
        registrationRequestDTO.setPasswordReg("!@#$%QWERTy12345");
        registrationRequestDTO.setPasswordReg2("!@#$%QWERTy12345");
    }

    @Test
    void checkRegistrationData_shouldReturnEmptyResultMapWhenUserIsCorrect(){
        //when
        Map<String, String> result = registerHelper.checkRegistrationData(registrationRequestDTO);
        //then
        assertTrue(result.isEmpty());
    }

    @Test
    void checkRegistrationData_shouldReturnNicknameError() {
        //given
        registrationRequestDTO.setNickname("");
        //when
        Map<String, String> result = registerHelper.checkRegistrationData(registrationRequestDTO);
        //then
        assertEquals(1, result.size());
        assertEquals("Długość musi być między 5 a 50 znaków", result.get("nickname"));
    }

    @Test
    void checkRegistrationData_shouldReturnNameError() {
        //given
        registrationRequestDTO.setName("");
        //when
        Map<String, String> result = registerHelper.checkRegistrationData(registrationRequestDTO);
        //then
        assertEquals(1, result.size());
        assertEquals("Długość musi być między 5 a 50 znaków", result.get("name"));
    }

    @Test
    void checkRegistrationData_shouldReturnSurnameError() {
        //given
        registrationRequestDTO.setSurname("");
        //when
        Map<String, String> result = registerHelper.checkRegistrationData(registrationRequestDTO);
        //then
        assertEquals(1, result.size());
        assertEquals("Długość musi być między 5 a 50 znaków", result.get("surname"));
    }

    @Test
    void checkRegistrationData_shouldReturnEmailError() {
        //given
        registrationRequestDTO.setEmailReg("");
        //when
        Map<String, String> result = registerHelper.checkRegistrationData(registrationRequestDTO);
        //then
        assertEquals(1, result.size());
        assertEquals("Długość musi być między 5 a 50 znaków", result.get("emailReg"));
    }

    @Test
    void checkRegistrationData_shouldReturnPasswordNotMachError() {
        //given
        registrationRequestDTO.setPasswordReg("asdaasdasd");
        //when
        Map<String, String> result = registerHelper.checkRegistrationData(registrationRequestDTO);
        //then
        assertEquals(1, result.size());
        assertEquals("Hasła nie są zgodne", result.get("passwordReg2"));
    }

    @Test
    void checkRegistrationData_shouldReturnPasswordMatcherError() {
        //given
        registrationRequestDTO.setPasswordReg("!@#$%^%");
        registrationRequestDTO.setPasswordReg2("!@#$%^%");
        //when
        Map<String, String> result = registerHelper.checkRegistrationData(registrationRequestDTO);
        //then
        assertEquals(1, result.size());
        assertEquals("Hasło nie spełnia wymagań", result.get("passwordReg"));
    }

    @Test
    void checkRegistrationData_shouldReturnAllErrors() {
        //given
        RegistrationRequestDTO registrationRequestDTO = new RegistrationRequestDTO();
        //when
        Map<String, String> result = registerHelper.checkRegistrationData(registrationRequestDTO);
        //then
        assertEquals(5, result.size());
        assertAll(
                () -> assertEquals("Długość musi być między 5 a 50 znaków", result.get("nickname")),
                () -> assertEquals("Długość musi być między 5 a 50 znaków", result.get("name")),
                () -> assertEquals("Długość musi być między 5 a 50 znaków", result.get("surname")),
                () -> assertEquals("Długość musi być między 5 a 50 znaków", result.get("emailReg")),
                () -> assertEquals("Hasło nie spełnia wymagań", result.get("passwordReg"))
        );

    }


}