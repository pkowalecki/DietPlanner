package pl.kowalecki.dietplanner.model.DTO.meal;

import lombok.Getter;
@Getter
public class MealNameDTO{
    private Long mealId;
    private String name;
    private String mealTypes;
    private Long userId;
}

