package pl.kowalecki.dietplanner.model.DTO.meal;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class IngredientDTO {
    private String ingredientName;
    private Double ingredientAmount;
    private String ingredientUnit;
    private Double measurementValue;
    private String measurementType;
    private Long ingredientNameId;
}
