package pl.kowalecki.dietplanner.model.ingredient.ingredientAmount;

import lombok.Getter;

import java.util.*;

@Getter
public enum IngredientUnit {
    GRAM("gram", "g"),
    MILILITR("mililitrów", "ml"),
    KILOGRAM("kilogramów", "kg");

    String fullName;
    String shortName;

    IngredientUnit(String fullName, String shortName) {
        this.fullName = fullName;
        this.shortName = shortName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public static IngredientUnit getByShortName(String shortName) {
        for (IngredientUnit ingredientUnit : values()) {
            if (ingredientUnit.getShortName().equals(shortName)) return ingredientUnit;
        }
        return null;
    }

    public static List<String> getAllIngredientNames(){
        List<String> ingredientNames = new ArrayList<>();
        for (IngredientUnit ingredientUnit: values()){
            ingredientNames.add(ingredientUnit.getShortName());
        }
        return ingredientNames;

    }

    public static Map<IngredientUnit, List<String>> getIngredientUnitMap(){
        Map<IngredientUnit, List<String>> ingredientUnitMap = new HashMap<>();
        for(IngredientUnit ingredientUnit : values()){
            List<String> ingredientValues = new ArrayList<>();
            if (!ingredientUnitMap.containsKey(ingredientUnit)) {
                ingredientValues.add(ingredientUnit.getFullName());
                ingredientValues.add(ingredientUnit.getShortName());
            }
            ingredientUnitMap.put(ingredientUnit, ingredientValues);
        }
        return ingredientUnitMap;
    }


}
