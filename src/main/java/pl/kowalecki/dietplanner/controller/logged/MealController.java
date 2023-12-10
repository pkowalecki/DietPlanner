package pl.kowalecki.dietplanner.controller.logged;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.kowalecki.dietplanner.model.ingredient.Ingredient;
import pl.kowalecki.dietplanner.model.ingredient.ingredientAmount.IngredientUnit;
import pl.kowalecki.dietplanner.model.ingredient.ingredientMeasurement.MeasurementType;
import pl.kowalecki.dietplanner.model.Meal;
import pl.kowalecki.dietplanner.repository.MealRepositoryImplementation;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RequestMapping("/auth/meal")
@RestController
public class MealController {

    private MealRepositoryImplementation mealRepository;

    @Autowired
    public MealController(MealRepositoryImplementation mealRepository) {
        this.mealRepository = mealRepository;
    }


    @GetMapping( "/allMeal")
    public ResponseEntity<List<Meal>> getListMeal(){
        if (!mealRepository.getAllMeals().isEmpty()) return new ResponseEntity<>(mealRepository.getAllMeals(), HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/getMeal/{id}")
    public ResponseEntity<Meal> getMealById(@PathVariable Long id){
        if (mealRepository.getMealById(id) != null) return new ResponseEntity<>(mealRepository.getMealById(id), HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping( "/addMeal")
    public ResponseEntity<?> addMeal(@RequestParam(name = "userId") String userId, @RequestBody Meal newMeal){

//        System.out.println(userId + " " + newMeal);
        mealRepository.addMeal(userId, newMeal);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/getMealIngredientsList/{id}")
    public ResponseEntity<List<Ingredient>> getMealIngredientsByMealId(@PathVariable Long id){
        if (mealRepository.getMealIngredientsByMealId(id) != null) return new ResponseEntity<>(mealRepository.getMealIngredientsByMealId(id), HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/getMealIngredientsFinalList")
    public ResponseEntity<List<Ingredient>> getMealIngredientsFinalList(@RequestParam List<Long> ids){
        return new ResponseEntity<>(mealRepository.getMealIngredientsFinalList(ids), HttpStatus.OK);
    }

    @GetMapping(value = "/getIngredientMap")
    public ResponseEntity<Map<IngredientUnit,List<String>>> getListIngredientUnit(){
        return new ResponseEntity<>(mealRepository.getIngredientUnitMap(), HttpStatus.OK);
    }
    @GetMapping(value = "/getMeasurementMap")
    public ResponseEntity<Map<MeasurementType, List<String>>> getListMeasurementName(){
        return new ResponseEntity<>(mealRepository.getMeasurementTypeMap(), HttpStatus.OK);
    }

}
