package pl.kowalecki.dietplanner.model.ingredient;

import lombok.*;
import pl.kowalecki.dietplanner.model.DTO.meal.IngredientNameDTO;
import pl.kowalecki.dietplanner.model.ingredient.ingredientAmount.IngredientUnit;
import pl.kowalecki.dietplanner.model.ingredient.ingredientMeasurement.MeasurementType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class IngredientTDTO {
    private IngredientNameDTO name;
    private Double ingredientAmount;
    private IngredientUnit ingredientUnit;
    private Double measurementValue;
    private MeasurementType measurementType;
    private Long ingredientNameId;
}