package pl.kowalecki.dietplanner.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kowalecki.dietplanner.Model.Meal;
import pl.kowalecki.dietplanner.Repository.MealRepositoryImplementation;

import java.util.List;

@RequestMapping("/meal")
@RestController
public class MealController {

    MealRepositoryImplementation mealRepository;

    @Autowired
    public MealController(MealRepositoryImplementation mealRepository) {
        this.mealRepository = mealRepository;
    }

    @GetMapping("allMeal")
    public ResponseEntity<List<Meal>> getListMeal(){
        if (!mealRepository.getAllMeals().isEmpty()) return new ResponseEntity<>(mealRepository.getAllMeals(), HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("getMeal/{id}")
    public ResponseEntity<Meal> getMealById(@PathVariable Long id){
        if (mealRepository.getMealById(id) != null) return new ResponseEntity<>(mealRepository.getMealById(id), HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("addMeal")
    public ResponseEntity<Meal> addMeal(@RequestBody Meal newMeal){
        if (mealRepository.addMeal(newMeal)) return new ResponseEntity<>(HttpStatus.CREATED);
        else return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
