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

@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")
        })
@Getter
@Setter
@NoArgsConstructor
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Name(value = "id")
    private Integer id;

    @Name(value = "name")
    private String name;

    @Name(value = "surname")
    private String surname;

    @Column(unique = true, nullable = false)
    @Name(value = "email")
    private String email;

    @Column(unique = true, nullable = false)
    @Name(value = "nickName")
    private String nickName;

    @Column(nullable = false)
    @JsonIgnore
    @Name(value = "password")
    @ToString.Exclude
    private String password;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @Name(value = "roles")
    private Set<Role> roles = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_meal",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "meal_id"))
    @Name(value = "mealList")
    private List<Meal> mealList;

    @Name(value = "isActive")
    boolean isActive;

    @Name(value = "hash")
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
