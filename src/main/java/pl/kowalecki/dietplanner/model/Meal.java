package pl.kowalecki.dietplanner.model;

import lombok.*;
import pl.kowalecki.dietplanner.model.DTO.MealType;
import pl.kowalecki.dietplanner.model.ingredient.IngredientDTO;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Meal {
    private Long mealId;
    private LocalDateTime additionDate;
    private LocalDateTime editDate;
    private String name;
    private String description;
    private String recipe;
    private List<IngredientDTO> ingredients;
    private String notes;
    private List<MealType> mealTypes;
    private Double portions;
    private boolean isDeleted;
}
