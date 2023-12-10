package pl.kowalecki.dietplanner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.kowalecki.dietplanner.model.ingredient.Ingredient;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "administrationusers",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")
        })
@Getter
@Setter
@NoArgsConstructor
@ToString
public class AdministrationUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String surname;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private String password;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "administrationuser_roles",
            joinColumns = @JoinColumn(name = "administrationuser_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "administrationuser_meal",
            joinColumns = @JoinColumn(name = "administrationuser_id"),
            inverseJoinColumns = @JoinColumn(name = "meal_id"))
    private List<Meal> mealList;

    public AdministrationUser(Integer id, String name, String surname, String email, Set<Role> roles) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.roles = roles;

    }

    public AdministrationUser(String name, String surname, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }
}
