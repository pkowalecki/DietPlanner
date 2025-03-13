package pl.kowalecki.dietplanner.service.meal.mealBoard;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import pl.kowalecki.dietplanner.client.dpa.meal.DietPlannerApiClient;
import pl.kowalecki.dietplanner.model.DTO.meal.MealNameDTO;
import pl.kowalecki.dietplanner.utils.DateUtils;
import pl.kowalecki.dietplanner.utils.MapUtils;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class MealBoardService implements IMealBoardService{

    private final DietPlannerApiClient dpaClient;
    @Override
    public Mono<String> getMealBoardPage(Model model) {
        return dpaClient.getMealsToBoard()
                .map(mealList -> {
                    model.addAttribute("days", DateUtils.getDaysOfWeek());
                    model.addAllAttributes(addMealsByType(mealList));
                    return "pages/logged/foodBoardPage";
                })
                .defaultIfEmpty("pages/logged/foodBoardPage");
    }

    private Map<String,List<Map<String,String>>> addMealsByType(List<MealNameDTO> mealList) {
        Map<String, List<Map<String, String>>> mealsByType = new HashMap<>();
        List<String> mealTypes = List.of("BREAKFAST", "SNACK", "LUNCH", "SUPPER");

        for (String type : mealTypes) {
            List<Map<String, String>> mappedMeals = MapUtils.mapToFtl(
                    mealList.stream()
                            .filter(meal -> Arrays.asList(meal.getMealTypes().split(",\\s*"))
                                    .contains(type))
                            .collect(Collectors.toList()),
                    "mealId", "name"
            );
            mealsByType.put(type.toLowerCase() + "List", mappedMeals);
        }
        return mealsByType;
    }
}
