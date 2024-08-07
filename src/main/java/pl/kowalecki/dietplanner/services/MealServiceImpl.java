package pl.kowalecki.dietplanner.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kowalecki.dietplanner.IngredientsListHelper;
import pl.kowalecki.dietplanner.model.DTO.IngredientToBuyDTO;
import pl.kowalecki.dietplanner.model.Meal;
import pl.kowalecki.dietplanner.model.User;
import pl.kowalecki.dietplanner.model.ingredient.Ingredient;
import pl.kowalecki.dietplanner.model.ingredient.ingredientAmount.IngredientUnit;
import pl.kowalecki.dietplanner.model.ingredient.ingredientMeasurement.MeasurementType;
import pl.kowalecki.dietplanner.repository.IngredientRepository;
import pl.kowalecki.dietplanner.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class MealServiceImpl implements MealService{

    private final MealRepository mealRepository;
    private final IngredientRepository ingredientRepository;
    private final UserServiceImpl userService;

    @Override
    public List<Meal> getAllMeals(){
        return mealRepository.findAll();
    }

    @Override
    public Meal getMealById(Long id){
        return mealRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Meal not found with id: " + id));
    }

    @Override
    public boolean deleteMealById(Long id){
//        try {
//            mealRepository.deleteById(id);
//            return true;
//        }catch (HibernateError error){
//            error.printStackTrace();
//            return false;
//        }
        return true;
    }

    @Override
    @Transactional
    public void addMeal(String userId, Meal newMeal) {

        if (newMeal.getIngredients() == null) {
            newMeal.setIngredients(new ArrayList<>());
        }
        Optional<User> userOptional = userService.findById(Integer.valueOf(userId));

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

    public Map<Boolean, List<Ingredient>> getMealTypeAndIngredientsByMealId(Long mealId){
        Map<Boolean, List<Ingredient>> map = new HashMap<>();
        Meal meal = mealRepository.findById(mealId).orElse(null);
        if (meal == null) {
            return map;
        }
        map.put(meal.getMealTypes().stream()
                .anyMatch(type -> "SNACK".equals(type.getMealTypenEn())), meal.getIngredients());
        return map;
    }

    public List<IngredientToBuyDTO> getMealIngredientsFinalList(List<Long> ids, Double multiplier) {
        List<Ingredient> combinedIngredients = new ArrayList<>();

        for (Long id : ids) {
            if (id == 0) continue;
            List<Ingredient> ingredients = getMealIngredientsByMealId(id);
            combinedIngredients.addAll(ingredients);
        }
        List<Ingredient> ingredients = IngredientsListHelper.prepareIngredientsList(combinedIngredients, multiplier);

        List<IngredientToBuyDTO> ingredientsToBuy = new ArrayList<>();

        for (Ingredient ingredient : ingredients){
            IngredientToBuyDTO ingredientDTO = new IngredientToBuyDTO(ingredient.getName(), ingredient.getIngredientAmount().toString(), ingredient.getIngredientUnit().getShortName(), ingredient.getMeasurementValue().toString(), ingredient.getMeasurementType().getMeasurementName().toString());
            ingredientsToBuy.add(ingredientDTO);
        }


        return ingredientsToBuy;
    }
    public Map<IngredientUnit, List<String>> getIngredientUnitMap(){
        Map<IngredientUnit, List<String>> ingredientListMap = IngredientUnit.getIngredientUnitMap();
        return ingredientListMap;
    }
    public  Map<MeasurementType, List<String>> getMeasurementTypeMap(){
        Map<MeasurementType, List<String>> measurementNames = MeasurementType.getMeasurementTypeMap();
        return measurementNames;
    }

    @Override
    public List<Meal> getMealByUserId(Long userId) {
        return mealRepository.findMealsByUserId(userId);
    }


    public List<String> getMealNamesByIdList(List<Long> list) {
        List<String> mealNames = new ArrayList<>();
        for (Long mealId : list){
            if (mealId == 0){
                mealNames.add("-");
                continue;
            }
            mealNames.add(mealRepository.getMealNameByMealId(mealId));
        }
        return mealNames;
    }
}
