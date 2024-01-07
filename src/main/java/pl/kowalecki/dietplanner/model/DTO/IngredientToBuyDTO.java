package pl.kowalecki.dietplanner.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IngredientToBuyDTO {

    String name;
    String ingredientAmount;
    String ingredientUnit;


    String measurementAmount;
    String measurementUnit;

}
