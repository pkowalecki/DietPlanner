package pl.kowalecki.dietplanner.model;

import lombok.*;
import pl.kowalecki.dietplanner.model.enums.MealType;
import pl.kowalecki.dietplanner.model.ingredient.Ingredient;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Meal implements Serializable {

    private Long mealId;
    private LocalDateTime additionDate;
    private LocalDateTime editDate;
    private String name;
    private String description;
    private String recipe;
    private List<Ingredient> ingredients;
    private String notes;
    private List<MealType> mealTypes;
    private boolean isDeleted;

}
