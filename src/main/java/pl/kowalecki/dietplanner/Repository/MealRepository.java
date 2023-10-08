package pl.kowalecki.dietplanner.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kowalecki.dietplanner.Model.Meal;


@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {

}
