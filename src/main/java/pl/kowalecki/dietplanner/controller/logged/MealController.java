package pl.kowalecki.dietplanner.controller.logged;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kowalecki.dietplanner.model.DTO.IngredientToBuyDTO;
import pl.kowalecki.dietplanner.model.ingredient.Ingredient;
import pl.kowalecki.dietplanner.model.ingredient.ingredientAmount.IngredientUnit;
import pl.kowalecki.dietplanner.model.ingredient.ingredientMeasurement.MeasurementType;
import pl.kowalecki.dietplanner.model.Meal;
import pl.kowalecki.dietplanner.services.MealServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RequestMapping("/auth/meal")
@RestController
@AllArgsConstructor
public class MealController {

    private MealServiceImpl mealService;

    @GetMapping( "/allMeal")
    public ResponseEntity<List<Meal>> getListMeal(){
        if (!mealService.getAllMeals().isEmpty()) return new ResponseEntity<>(mealService.getAllMeals(), HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/getMeal/{id}")
    public ResponseEntity<Meal> getMealById(@PathVariable Long id){
        if (mealService.getMealById(id) != null) return new ResponseEntity<>(mealService.getMealById(id), HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping(value = "/deleteMeal/{id}")
    public ResponseEntity<?> deleteMealById(@PathVariable Long id) {
        if (mealService.getMealById(id) != null)
            return new ResponseEntity<>(mealService.deleteMealById(id), HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping( "/addMeal")
    public ResponseEntity<?> addMeal(@RequestParam(name = "userId") String userId, @RequestBody Meal newMeal){
        mealService.addMeal(userId, newMeal);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @PostMapping("/generateFoodBoard")
    public ResponseEntity<List<IngredientToBuyDTO>> generateFoodBoard(@RequestParam("ids") String ids, @RequestParam("multiplier") Double multiplier) {
        System.out.println("ids: " + ids);
        List<Long> idsList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        return new ResponseEntity<>(mealService.getMealIngredientsFinalList(idsList, multiplier), HttpStatus.OK);
    }
//    @PostMapping("/generateFoodRecipe")
//    public ResponseEntity<List<FoodDTO>> generateFoodRecipe(@RequestParam("ids") String ids, @RequestParam("multiplier") Double multiplier) {
//        List<Long> idsList = Arrays.stream(ids.split(","))
//                .map(Long::parseLong)
//                .collect(Collectors.toList());
//        return new ResponseEntity<>(mealRepository.getMealRecipeFinalList(idsList, multiplier), HttpStatus.OK);
//    }

    @GetMapping(value = "/getMealIngredientsList/{id}")
    public ResponseEntity<List<Ingredient>> getMealIngredientsByMealId(@PathVariable Long id){
        if (mealService.getMealIngredientsByMealId(id) != null) return new ResponseEntity<>(mealService.getMealIngredientsByMealId(id), HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/getIngredientMap")
    public ResponseEntity<Map<IngredientUnit,List<String>>> getListIngredientUnit(){
        return new ResponseEntity<>(mealService.getIngredientUnitMap(), HttpStatus.OK);
    }
    @GetMapping(value = "/getMeasurementMap")
    public ResponseEntity<Map<MeasurementType, List<String>>> getListMeasurementName(){
        return new ResponseEntity<>(mealService.getMeasurementTypeMap(), HttpStatus.OK);
    }

    @GetMapping(value = "/getMealsByUserId/{id}")
    public ResponseEntity<List<Meal>> getMealsByUserId(@PathVariable Long id){
        if (id != null) return new ResponseEntity<>(mealService.getMealByUserId(id), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
