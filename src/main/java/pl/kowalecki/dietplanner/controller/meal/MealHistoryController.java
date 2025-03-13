package pl.kowalecki.dietplanner.controller.meal;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.kowalecki.dietplanner.service.meal.mealHistory.IMealHistoryService;
import reactor.core.publisher.Mono;

@RequestMapping("/auth/meal-history")
@Controller
@AllArgsConstructor
@Slf4j
public class MealHistoryController {

    private final IMealHistoryService mealHistoryService;

    @GetMapping(value = "/")
    public Mono<String> mealsHistory(Model model) {
        return mealHistoryService.getMealHistories(model);
    }

    @PostMapping(value = "/mealHistory")
    public Mono<String> getMealHistoryPage(@RequestParam("id") String historyId, Model model) {
        return mealHistoryService.getMealHistory(historyId, model);

    }
}
