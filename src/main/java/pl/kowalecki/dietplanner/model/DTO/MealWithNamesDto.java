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
public class MealWithNamesDto {

    private List<FoodDTO> foodList;
    private List<String> meals;

}
