package pl.kowalecki.dietplanner.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IngredientToBuy {

    String name;
    String ingredientAmount;
    String ingredientUnit;
    String measurementAmount;
    String measurementUnit;

}
