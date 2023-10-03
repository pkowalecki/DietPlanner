package pl.kowalecki.dietplanner.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kowalecki.dietplanner.Model.Meal;

import java.util.List;

public interface MealRepository extends JpaRepository<Meal, Long> {

    List<Meal> getAllMeals();
    Meal getMealById(Long id);
    void addMeal(Meal meal);
    void updateMeal(Meal meal);
    void removeMealById(Long id);

}
