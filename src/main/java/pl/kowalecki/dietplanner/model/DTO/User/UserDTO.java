package pl.kowalecki.dietplanner.model.DTO.User;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Integer id;
    private String name;
    private String surname;
    private String email;
    private String nickName;
    private List<String> roles = new ArrayList<>();
}
