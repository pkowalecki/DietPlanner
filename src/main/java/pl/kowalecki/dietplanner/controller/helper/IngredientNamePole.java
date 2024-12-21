package pl.kowalecki.dietplanner.controller.helper;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum IngredientNamePole {

    INGREDIENT_NAME("ingredientName"),
    INGREDIENT_BRAND("ingredientBrand"),
    PROTEIN("protein"),
    CARBOHYDRATES("carbohydrates"),
    FAT("fat"),
    KCAL("KCAL"),

    ;

    private String fieldName;

}
