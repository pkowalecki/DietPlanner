package pl.kowalecki.dietplanner.model.DTO.meal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MealMainInfoDTO {
    Long mealId;
    String name;
    String description;
    Long userId;


}
