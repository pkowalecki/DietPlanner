package pl.kowalecki.dietplanner.Repository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.kowalecki.dietplanner.Model.Meal;

import java.util.List;

@Repository
public class MealRepositoryImplementation{

    private final MealRepository mealRepository;

    @Autowired
    public MealRepositoryImplementation(MealRepository mealRepository){
        this.mealRepository=mealRepository;
    }

    public List<Meal> getAllMeals(){
        return mealRepository.findAll();
    }

    public Meal getMealById(Long id){
        return mealRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Meal not found with id: " + id));
    }

    public boolean addMeal(Meal meal){
        if (meal!=null){
            mealRepository.save(meal);
            return true;
        }else {
            return false;
        }

    }

}
