package pl.kowalecki.dietplanner.model.ingredient;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
public class IngredientName {

    @JsonProperty("ingredientName")
    private String name;
    @JsonProperty("ingredientBrand")
    private String brand;
    private double protein;
    private double carbohydrates;
    private double fat;
    private double kcal;
    private String ingredientId;
}
