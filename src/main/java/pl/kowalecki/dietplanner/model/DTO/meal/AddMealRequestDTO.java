package pl.kowalecki.dietplanner.model.DTO.meal;

import lombok.*;

import java.util.List;


public record AddMealRequestDTO(
        String mealName,
        String description,
        String recipe,
        String notes,
        List<IngredientDTO> ingredients,
        List<String> mealTypes,
        double portions
) {
    @Getter
    public enum AddMealPole {

        MEAL_NAME("mealName"),
        DESCRIPTION("description"),
        RECIPE("recipe"),
        NOTES("notes"),
        MEAL_TYPES_SELECT("mealTypesSelect"),

        INGREDIENT("ingredient"),
        PORTIONS("portions"),
        ;

        private final String fieldName;

        AddMealPole(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getFieldName() {
            return fieldName;
        }
    }
}
