package pl.kowalecki.dietplanner.model.DTO.User;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    int id;
    String name;
    String surname;
    String email;
    String nickName;
    List<String> roles;
}
