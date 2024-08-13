package pl.kowalecki.dietplanner.model.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
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