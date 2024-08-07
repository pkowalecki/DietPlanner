package pl.kowalecki.dietplanner.services;

import pl.kowalecki.dietplanner.model.DTO.IngredientToBuyDTO;
import pl.kowalecki.dietplanner.model.Meal;
import pl.kowalecki.dietplanner.model.ingredient.Ingredient;
import pl.kowalecki.dietplanner.model.ingredient.ingredientAmount.IngredientUnit;
import pl.kowalecki.dietplanner.model.ingredient.ingredientMeasurement.MeasurementType;

import java.util.List;
import java.util.Map;

public interface MealService {

    List<Meal> getAllMeals();
    Meal getMealById(Long id);
    boolean deleteMealById(Long id);
    void addMeal(String userId, Meal newMeal);
    List<Meal> getMealByUserId(Long userId);
}
