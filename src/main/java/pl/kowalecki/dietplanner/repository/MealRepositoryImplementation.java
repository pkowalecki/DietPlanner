package pl.kowalecki.dietplanner.repository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.kowalecki.dietplanner.IngredientsListHelper;
import pl.kowalecki.dietplanner.model.AdministrationUser;
import pl.kowalecki.dietplanner.model.ingredient.Ingredient;
import pl.kowalecki.dietplanner.model.ingredient.ingredientAmount.IngredientUnit;
import pl.kowalecki.dietplanner.model.ingredient.ingredientMeasurement.MeasurementType;
import pl.kowalecki.dietplanner.model.Meal;

import java.time.LocalDateTime;
import java.util.*;

@Repository
public class MealRepositoryImplementation{

    private final MealRepository mealRepository;
    private final IngredientRepository ingredientRepository;

    @Autowired
    private AdministrationUserRepository administrationUserRepository;

    @Autowired
    public MealRepositoryImplementation(MealRepository mealRepository, IngredientRepository ingredientRepository){
        this.mealRepository=mealRepository;
        this.ingredientRepository=ingredientRepository;
    }

    public List<Meal> getAllMeals(){
        return mealRepository.findAll();
    }

    public Meal getMealById(Long id){
        return mealRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Meal not found with id: " + id));
    }

    @Transactional
    public void addMeal(String userId, Meal newMeal) {

        if (newMeal.getIngredients() == null) {
            newMeal.setIngredients(new ArrayList<>());
        }
        newMeal.setAdditionDate(LocalDateTime.now());
        Meal savedMeal = mealRepository.save(newMeal);

        for (Ingredient ingredient : newMeal.getIngredients()) {
            ingredient.setMeal(savedMeal);
            ingredientRepository.save(ingredient);
        }
    }

    public List<Ingredient> getMealIngredientsByMealId(Long mealId){
    Meal meal = mealRepository.findById(mealId).orElse(null);
        if (meal == null) {
            return Collections.emptyList();
        }
            return meal.getIngredients();
    }

    public List<Ingredient> getMealIngredientsFinalList(List<Long> ids) {
        List<Ingredient> combinedIngredients = new ArrayList<>();

        for (Long id : ids) {
            List<Ingredient> ingredients = getMealIngredientsByMealId(id);
            combinedIngredients.addAll(ingredients);
        }
        List<Ingredient> ingredients = IngredientsListHelper.prepareIngredientsList(combinedIngredients);

        return ingredients;
    }
    public Map<IngredientUnit, List<String>> getIngredientUnitMap(){
        Map<IngredientUnit, List<String>> ingredientListMap = IngredientUnit.getIngredientUnitMap();
        return ingredientListMap;
    }
    public  Map<MeasurementType, List<String>> getMeasurementTypeMap(){
        Map<MeasurementType, List<String>> measurementNames = MeasurementType.getMeasurementTypeMap();
        return measurementNames;
    }

}
