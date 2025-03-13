package pl.kowalecki.dietplanner.controller.meal;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.kowalecki.dietplanner.service.meal.mealBoard.IMealBoardService;
import reactor.core.publisher.Mono;


@RequestMapping("/auth/meal-board")
@Controller
@AllArgsConstructor
@Slf4j
public class MealBoardController {

    private IMealBoardService mealBoardService;

    @GetMapping(value = "/")
    public Mono<String> mealPage(Model model) {
        return mealBoardService.getMealBoardPage(model);
    }
}
