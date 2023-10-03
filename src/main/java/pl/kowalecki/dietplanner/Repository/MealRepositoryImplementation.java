package pl.kowalecki.dietplanner.Repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import pl.kowalecki.dietplanner.Model.Meal;

import java.util.List;

@Service
public class MealRepositoryImplementation{

    private final MealRepository mealRepository;

    @Autowired
    public MealRepositoryImplementation(MealRepository mealRepository){
        this.mealRepository=mealRepository;
    }

    Meal getMealById(Long id){
        return mealRepository.getMealById(id);
    }

    List<Meal> getAllMeals(){
        return mealRepository.getAllMeals();
    }



}
