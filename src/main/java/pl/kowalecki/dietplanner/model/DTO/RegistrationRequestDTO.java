package pl.kowalecki.dietplanner.model.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class RegistrationRequestDTO {

    private String nickname;
    private String name;
    private String surname;
    private String email;
    private String password;
    private Set<String> role;
}
