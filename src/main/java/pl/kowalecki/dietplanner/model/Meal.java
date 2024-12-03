package pl.kowalecki.dietplanner.model;

import lombok.*;
import pl.kowalecki.dietplanner.model.enums.MealType;
import pl.kowalecki.dietplanner.model.ingredient.IngredientDTO;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Meal{

    private String name;
    private String description;
    private String recipe;
    private List<IngredientDTO> ingredients;
    private String notes;
    private List<MealType> mealTypes;
    private boolean isDeleted;
    private int portions;

}
