package pl.kowalecki.dietplanner.model.DTO.meal;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AddMealRequestDTO {
    private String mealName;
    private String description;
    private String recipe;
    private String notes;
    private List<IngredientDTO> ingredients;
    private List<String> mealTypes;
}
