package pl.kowalecki.dietplanner.model.DTO.meal;

import lombok.*;
import pl.kowalecki.dietplanner.model.DTO.IngredientUnit;
import pl.kowalecki.dietplanner.model.DTO.MeasurementType;
import pl.kowalecki.dietplanner.model.enums.MealType;
import pl.kowalecki.dietplanner.model.ingredient.IngredientName;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class MealBaseDTO {
    private List<IngredientName> ingredientNameList;
    private List<MealType> mealTypeList;
    private List<IngredientUnit> ingredientUnitList;
    private List<MeasurementType> measurementTypeList;
}
