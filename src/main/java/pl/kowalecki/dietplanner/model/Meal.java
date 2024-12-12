package pl.kowalecki.dietplanner.model;

import pl.kowalecki.dietplanner.model.enums.MealType;
import pl.kowalecki.dietplanner.model.ingredient.IngredientDTO;

import java.util.List;


public record Meal(
        String name,
        String description,
        String recipe,
        List<IngredientDTO> ingredients,
        String notes,
        List<MealType> mealTypes,
        boolean isDeleted,
        int portions

) {

}
