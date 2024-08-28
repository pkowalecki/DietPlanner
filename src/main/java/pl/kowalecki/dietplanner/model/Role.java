package pl.kowalecki.dietplanner.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.kowalecki.dietplanner.model.enums.EnumRole;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Role {
    private Integer id;
    private EnumRole name;
}
