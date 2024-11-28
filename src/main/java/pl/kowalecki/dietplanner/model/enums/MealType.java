package pl.kowalecki.dietplanner.model.enums;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public enum MealType {

    BREAKFAST("śniadanie", "breakfast"),
    SNACK("przekąska", "snack"),
    LUNCH("obiad", "lunch"),
    SUPPER("kolacja", "supper"),
    OTHER("inne", "other")
    ;

    private String mealTypePl;
    private String mealTypenEn;


    public static MealType getByShortName(String shortName) {
        for (MealType ingredientUnit : values()) {
            if (ingredientUnit.getMealTypePl().equals(shortName)) return ingredientUnit;
        }
        return null;
    }

}
