package pl.kowalecki.dietplanner.service.meal.mealBoard;

import org.springframework.ui.Model;
import reactor.core.publisher.Mono;

public interface IMealBoardService {
    Mono<String> getMealBoardPage(Model model);
}
