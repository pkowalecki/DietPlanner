package pl.kowalecki.dietplanner.controller.helper;

import org.springframework.stereotype.Component;
import pl.kowalecki.dietplanner.model.DTO.meal.AddMealRequestDTO;
import pl.kowalecki.dietplanner.model.DTO.meal.IngredientDTO;
import pl.kowalecki.dietplanner.model.enums.MealType;
import pl.kowalecki.dietplanner.utils.TextTools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AddMealHelper {

    HashMap<String, String> errors = new HashMap<>();

    public Map<String, String> checkData(AddMealRequestDTO addMealRequestDTO) {
        errors = new HashMap<>();
        checkMealName(addMealRequestDTO.mealName());
        checkMealDescription(addMealRequestDTO.description());
        checkMealRecipe(addMealRequestDTO.recipe());
        checkMealNotes(addMealRequestDTO.notes());
        checkMealTypes(addMealRequestDTO.mealTypes());
        checkIngredients(addMealRequestDTO.ingredients());
        checkPortions(addMealRequestDTO.portions());
        return errors;
    }

    private void checkPortions(double portions) {
        if (!checkPortionsAmount(portions)){
            errors.put(AddMealRequestDTO.AddMealPole.PORTIONS.getFieldName(), "Portions should be between 0 and 1000");
        }
    }

    private void checkMealName(String mealName) {
        if (!TextTools.isTextLengthOk(mealName, 5, 254))
            errors.put(AddMealRequestDTO.AddMealPole.MEAL_NAME.getFieldName(), "Meal name should be between 5-254");
    }

    private void checkMealDescription(String description) {
        if (!TextTools.isTextLengthOk(description, 0, 10000))
            errors.put(AddMealRequestDTO.AddMealPole.DESCRIPTION.getFieldName(), "Description should be shorten than 10000");
    }

    private void checkMealRecipe(String recipe) {
        if (!TextTools.isTextLengthOk(recipe, 5, 10000))
            errors.put(AddMealRequestDTO.AddMealPole.RECIPE.getFieldName(), "Recipe should be between 5-10000");
    }

    private void checkMealNotes(String notes) {
        if (!TextTools.isTextLengthOk(notes, 0, 10000))
            errors.put(AddMealRequestDTO.AddMealPole.NOTES.getFieldName(), "Notes should be shorten than 10000");
    }

    public void checkMealTypes(List<String> mealTypes) {
        if (mealTypes == null || mealTypes.isEmpty()) {
            errors.put(AddMealRequestDTO.AddMealPole.MEAL_TYPES_SELECT.getFieldName(), "Meal type should be selected");
            return;
        }
        for (String mealType : mealTypes) {
            try {
                MealType.getByShortName(mealType);
            } catch (IllegalArgumentException e) {
                errors.put(AddMealRequestDTO.AddMealPole.MEAL_TYPES_SELECT.getFieldName(), "Invalid meal type: " + mealType);
            }
        }
    }

    private void checkIngredients(List<IngredientDTO> ingredients) {
        String ingredientName = "";
        if (ingredients == null || ingredients.isEmpty()) {
            errors.put(AddMealRequestDTO.AddMealPole.INGREDIENT.getFieldName(), "Ingredients should be selected");
            return;
        }

        for (IngredientDTO ingredient : ingredients) {
            ingredientName = ingredient.getIngredientName();
            if (checkIngredientName(ingredient.getIngredientNameId())) {
                errors.put(AddMealRequestDTO.AddMealPole.INGREDIENT.getFieldName(), ingredientName + " Invalid ingredient name");
                return;
            }

            if (checkIngredientUnit(ingredient.getIngredientUnit())) {
                errors.put(AddMealRequestDTO.AddMealPole.INGREDIENT.getFieldName(), ingredientName + " Invalid ingredient unit");
                return;
            }

            if (checkIngredientAmount(ingredient.getIngredientAmount())) {
                errors.put(AddMealRequestDTO.AddMealPole.INGREDIENT.getFieldName(), ingredientName + " Invalid ingredient amount");
                return;
            }

            if (checkMeasurementType(ingredient.getMeasurementType())) {
                errors.put(AddMealRequestDTO.AddMealPole.INGREDIENT.getFieldName(), ingredientName + " Invalid measurement type");
                return;
            }

            if (checkMeasurementValue(ingredient.getMeasurementValue())) {
                errors.put(AddMealRequestDTO.AddMealPole.INGREDIENT.getFieldName(), ingredientName + " Invalid measurement value");
                return;
            }
        }
    }

    private boolean checkIngredientName(Long ingredientNameId) {
        return ingredientNameId == null || ingredientNameId == 0;
    }

    private boolean checkIngredientUnit(String ingredientUnit) {
        return ingredientUnit == null || ingredientUnit.isEmpty();
    }

    private boolean checkIngredientAmount(Double ingredientAmount) {
        return ingredientAmount == null || ingredientAmount <= 0;
    }

    private boolean checkMeasurementType(String measurementType) {
        return measurementType == null || measurementType.isEmpty();
    }

    private boolean checkMeasurementValue(Double measurementValue) {
        return measurementValue == null || measurementValue <= 0;
    }

    private boolean checkPortionsAmount(Double portionsAmount){
        if(portionsAmount == null || portionsAmount <= 0){
            return false;
        }else return portionsAmount <= 1000;
    }
}
