package pl.kowalecki.dietplanner.model.DTO;

import lombok.Data;
import pl.kowalecki.dietplanner.model.DTO.meal.IngredientNameDTO;

import java.util.List;

@Data
public class MealStarterPack {
    List<IngredientNameDTO> ingredientNameList;
    List<MealType> mealTypeList;
    List<IngredientUnit> ingredientUnitList;
    List<MeasurementType> measurementTypeList;
}
