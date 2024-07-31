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

    @NotNull
    @NotEmpty(message = "Username is required")
    @Size(min = 5, max = 50, message = "Username must be between 5 and 50 characters")
    private String nickname;

    private String name;

    private String surname;

    @NotNull
    @NotEmpty(message = "Email is required")
    @Email(message = "Email should be valid")
    private String emailReg;

    @NotNull
    @NotEmpty(message = "Password is required")
    @Size(min = 8, max = 255, message = "Password must be at least 8 characters long")
    private String passwordReg;

    @NotNull
    @NotEmpty(message = "Password is required")
    @Size(min = 8, max = 255, message = "Password must be at least 8 characters long")
    private String passwordReg2;

    private Set<String> role;

}
