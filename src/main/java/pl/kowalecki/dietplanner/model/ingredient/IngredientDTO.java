package pl.kowalecki.dietplanner.model.ingredient;

import lombok.*;
import pl.kowalecki.dietplanner.model.DTO.IngredientUnit;
import pl.kowalecki.dietplanner.model.DTO.MeasurementType;
import pl.kowalecki.dietplanner.model.DTO.meal.IngredientNameDTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class IngredientDTO {
    private IngredientNameDTO name;
    private Double ingredientAmount;
    private IngredientUnit ingredientUnit;
    private Double measurementValue;
    private MeasurementType measurementType;
    private Long ingredientNameId;
}