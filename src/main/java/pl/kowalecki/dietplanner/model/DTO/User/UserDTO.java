package pl.kowalecki.dietplanner.model.DTO.User;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Integer id;
    private String email;
    private String name;
    private String nickName;
    private String surname;
    private List<String> roles;


//    public UserDTO(UserDetails userDetails) {
//        this.email = userDetails.getUsername();
//        this.roles = userDetails.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.toList());
//    }


}
