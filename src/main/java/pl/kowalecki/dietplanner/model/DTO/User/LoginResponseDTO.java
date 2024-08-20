package pl.kowalecki.dietplanner.model.DTO.User;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    String email;
    List<String> roles;
}
