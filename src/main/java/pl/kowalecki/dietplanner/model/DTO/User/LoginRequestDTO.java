package pl.kowalecki.dietplanner.model.DTO.User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginRequestDTO {
    private String email;
    @ToString.Exclude
    private String password;
}