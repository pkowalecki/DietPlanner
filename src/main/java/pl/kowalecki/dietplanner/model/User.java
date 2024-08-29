package pl.kowalecki.dietplanner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jdk.jfr.Name;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.Audited;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class User {
    private Integer id;
    private String name;
    private String surname;
    private String email;
    private String nickName;
    @JsonIgnore
    @ToString.Exclude
    private String password;
    private Set<Role> roles = new HashSet<>();
    private List<Meal> mealList;
    boolean isActive;
    String hash;

    public User(Integer id, String name, String nickName, String surname, String email, Set<Role> roles) {
        this.id = id;
        this.name = name;
        this.nickName = nickName;
        this.surname = surname;
        this.email = email;
        this.roles = roles;

    }

    public User(String name, String nickName, String surname, String email, String password, boolean isActive, String hash) {
        this.name = name;
        this.nickName = nickName;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.isActive = isActive;
        this.hash = hash;
    }
}
