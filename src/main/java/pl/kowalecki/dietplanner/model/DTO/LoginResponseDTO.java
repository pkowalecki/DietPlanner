package pl.kowalecki.dietplanner.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
public class LoginResponseDTO {
    String email;
    List<String> roles;
}
