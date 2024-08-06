package pl.kowalecki.dietplanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.kowalecki.dietplanner.model.Meal;

import java.util.List;



public interface MealRepository extends JpaRepository<Meal, Long> {

    @Query(value = "SELECT m.* FROM meals m " +
            "JOIN user_meal am ON m.meal_id = am.meal_id " +
            "WHERE am.user_id = :id",
            nativeQuery = true)
    List<Meal> findMealsByUserId(Long id);

    List<Meal> findMealsByMealIdIn(List<Long> mealIds);
}
