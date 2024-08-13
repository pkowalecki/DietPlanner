package pl.kowalecki.dietplanner.model.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString
public class RegistrationRequestDTO {

    private String nickname;
    private String name;
    private String surname;
    private String emailReg;
    private String passwordReg;
    private String passwordReg2;
    private Set<String> role;

}
