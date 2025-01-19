package pl.kowalecki.dietplanner.model.DTO.meal;

import lombok.*;
import pl.kowalecki.dietplanner.model.DTO.IngredientToBuy;
import pl.kowalecki.dietplanner.model.Meal;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class MealHistoryDetailsDTO {
        List<String> mealNames;
        Double multiplier;
        List<IngredientToBuy> ingredientsToBuy;
}
