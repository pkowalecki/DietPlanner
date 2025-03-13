package pl.kowalecki.dietplanner.service.meal.mealHistory;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import pl.kowalecki.dietplanner.client.dpa.meal.DietPlannerApiClient;
import pl.kowalecki.dietplanner.utils.DateUtils;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
@Slf4j
public class MealHistoryService implements IMealHistoryService {

    private final DietPlannerApiClient dpaClient;
    @Override
    public Mono<String> getMealHistories(Model model) {
        return dpaClient.getMealHistoryList()
                .flatMap(mealHistory -> {
                    model.addAttribute("mealHistory", mealHistory);
                    return Mono.just("pages/logged/mealsHistoryPage");
                });
    }

    @Override
    public Mono<String> getMealHistory(String historyId, Model model) {
        return dpaClient.getMealHistoryById(historyId)
                .map(mealHistory -> {
                    model.addAttribute("days", DateUtils.getDaysOfWeek());
                    model.addAttribute("mealHistory", mealHistory);
                    return "pages/logged/mealHistoryPage";
                });
    }
}
