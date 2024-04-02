package pl.kowalecki.dietplanner.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import pl.kowalecki.dietplanner.model.enums.MealType;
import pl.kowalecki.dietplanner.model.ingredient.Ingredient;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "meals")
public class Meal implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mealId;
    private LocalDateTime additionDate;
    private LocalDateTime editDate;
    private String name;
    @Column(columnDefinition="TEXT")
    private String description;
    @Column(columnDefinition="TEXT")
    private String recipe;

    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Ingredient> ingredients;
    @Column(columnDefinition="TEXT")
    private String notes;
    @Enumerated(EnumType.STRING)
    private List<MealType> mealTypes;

    private boolean isDeleted;



    public Meal(LocalDateTime additionDate, LocalDateTime editDate, String name, String description, String recipe, List<Ingredient> ingredients, String notes, boolean isDeleted) {
        this.additionDate = additionDate;
        this.editDate = editDate;
        this.name = name;
        this.description = description;
        this.recipe = recipe;
        this.ingredients = ingredients;
        this.notes = notes;
        this.isDeleted = isDeleted;
    }

    public Meal() {
    }


    public Long getId() {
        return mealId;
    }

    public void setId(Long mealId) {
        this.mealId = mealId;
    }


    public LocalDateTime getAdditionDate() {
        return additionDate;
    }

    public void setAdditionDate(LocalDateTime additionDate) {
        this.additionDate = additionDate;
    }

    public LocalDateTime getEditDate() {
        return editDate;
    }

    public void setEditDate(LocalDateTime editDate) {
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

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public List<MealType> getMealTypes() {
        return mealTypes;
    }

    public void setMealTypes(List<MealType> mealTypes) {
        this.mealTypes = mealTypes;
    }


    @Override
    public String toString() {
        return "Meal{" +
                "mealId=" + mealId +
                ", additionDate=" + additionDate +
                ", editDate=" + editDate +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", recipe='" + recipe + '\'' +
                ", ingredients=" + ingredients +
                ", notes='" + notes + '\'' +
                ", mealTypes=" + mealTypes +
                ", isDeleted=" + isDeleted +
                '}';
    }

}
