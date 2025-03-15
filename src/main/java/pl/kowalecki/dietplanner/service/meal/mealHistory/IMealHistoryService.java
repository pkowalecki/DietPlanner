package pl.kowalecki.dietplanner.service.meal.mealHistory;

import org.springframework.ui.Model;
import reactor.core.publisher.Mono;

public interface IMealHistoryService {
    Mono<String> getMealHistories(Model model);

    Mono<String> getMealHistory(String historyId, Model model);
}
