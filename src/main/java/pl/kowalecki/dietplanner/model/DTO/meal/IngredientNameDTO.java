package pl.kowalecki.dietplanner.model.DTO.meal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class IngredientNameDTO {

    private Long id;
    private String ingredientName;
    private String ingredientBrand;
    private int protein;
    private int carbohydrates;
    private int fat;
    private int kcal;
}
