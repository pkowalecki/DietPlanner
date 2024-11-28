package pl.kowalecki.dietplanner.model.DTO.meal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.kowalecki.dietplanner.model.DTO.IngredientToBuy;
import pl.kowalecki.dietplanner.model.Meal;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MealBoardDTO {

    List<Meal> mealList;
    List<IngredientToBuy> ingredientsToBuy;
}
