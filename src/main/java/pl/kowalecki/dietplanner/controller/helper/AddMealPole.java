package pl.kowalecki.dietplanner.controller.helper;

public enum AddMealPole {

    MEAL_NAME("mealName"),
    DESCRIPTION("description"),
    RECIPE("recipe"),
    NOTES("notes"),
    MEAL_TYPES_SELECT("mealTypesSelect"),

    INGREDIENT("ingredient"),
    ;

    private String fieldName;
    AddMealPole(String fieldName) {
        this.fieldName=fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
