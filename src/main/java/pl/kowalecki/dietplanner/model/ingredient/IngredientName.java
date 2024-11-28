package pl.kowalecki.dietplanner.model.ingredient;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
public class IngredientName {

    private Long id;
    @JsonProperty("ingredientName")
    private String name;
    @JsonProperty("ingredientBrand")
    private String brand;
    private int protein;
    private int carbohydrates;
    private int fat;
    private int kcal;
}
