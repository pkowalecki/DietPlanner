package pl.kowalecki.dietplanner.model;

import lombok.*;
import pl.kowalecki.dietplanner.model.DTO.MealType;
import pl.kowalecki.dietplanner.model.ingredient.IngredientDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Meal {
    private Long mealId =-1L;
    private LocalDateTime additionDate;
    private LocalDateTime editDate;
    private String name ="";
    private String description ="";
    private String recipe ="";
    private List<IngredientDTO> ingredients = new ArrayList<>();
    private String notes ="";
    private List<MealType> mealTypes = new ArrayList<>();
    private Double portions = 1.0;
    private boolean mealPublic;
}
