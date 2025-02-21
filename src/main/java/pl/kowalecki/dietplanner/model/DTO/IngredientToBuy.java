package pl.kowalecki.dietplanner.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IngredientToBuy {

    String name;
    String ingredientAmount;
    String ingredientUnit;
    private List<MeasurementDTO> measurementList;

}
