package pl.kowalecki.dietplanner.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.hibernate.envers.Audited;

@Entity
@Audited
public class Meal {

    public Meal(Long id, Long additionDate, Long editDate, String name, String description, String recipe, String ingredients) {
        this.id = id;
        this.additionDate = additionDate;
        this.editDate = editDate;
        this.name = name;
        this.description = description;
        this.recipe = recipe;
        this.ingredients = ingredients;
    }

    public Meal() {
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long additionDate;
    Long editDate;
    String name;
    String description;
    String recipe;
    String ingredients;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAdditionDate() {
        return additionDate;
    }

    public void setAdditionDate(Long additionDate) {
        this.additionDate = additionDate;
    }

    public Long getEditDate() {
        return editDate;
    }

    public void setEditDate(Long editDate) {
        this.editDate = editDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", additionDate=" + additionDate +
                ", editDate=" + editDate +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", recipe='" + recipe + '\'' +
                ", ingredients='" + ingredients + '\'' +
                '}';
    }
}
