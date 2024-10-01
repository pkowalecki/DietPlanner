package pl.kowalecki.dietplanner.model;

import lombok.*;
import pl.kowalecki.dietplanner.model.enums.MealType;
import pl.kowalecki.dietplanner.model.enums.MealTypeDTO;
import pl.kowalecki.dietplanner.model.ingredient.Ingredient;
import pl.kowalecki.dietplanner.model.ingredient.IngredientTDTO;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Meal{

//    private Long mealId;
    private LocalDateTime additionDate;
    private LocalDateTime editDate;
    private String name;
    private String description;
    private String recipe;
    private List<IngredientTDTO> ingredients;
    private String notes;
    private List<MealType> mealTypes;
    private boolean isDeleted;

}
