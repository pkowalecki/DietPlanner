package pl.kowalecki.dietplanner.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.kowalecki.dietplanner.model.ingredient.Ingredient;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class FoodDTO {

    String name;
    String recipe;
    String description;

    List<Ingredient> ingredients;
}
