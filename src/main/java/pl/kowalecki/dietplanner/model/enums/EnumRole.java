package pl.kowalecki.dietplanner.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public enum EnumRole {

    ROLE_ADMIN("admin"),
    ROLE_USER("user");


    private String name;

    public String getName() {
        return name;
    }

}

