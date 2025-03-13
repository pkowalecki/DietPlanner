package pl.kowalecki.dietplanner.controller.meal;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.kowalecki.dietplanner.model.DTO.meal.AddMealRequestDTO;
import pl.kowalecki.dietplanner.model.DTO.meal.MealMainInfoDTO;
import pl.kowalecki.dietplanner.model.WebPageResponse;
import pl.kowalecki.dietplanner.service.meal.mealManagement.IMealManagementService;
import reactor.core.publisher.Mono;

import java.util.List;

@RequestMapping("/auth/meals")
@Controller
@AllArgsConstructor
public class MealManagementController {

    private final IMealManagementService mealService;

    @GetMapping(value = "/")
    public Mono<String> getAddMealPage(Model model) {
        return mealService.getAddMealPage(model);
    }

    @GetMapping(value = "/editMeal/{id}")
    public Mono<String> getEditMealPage(@PathVariable Long id, Model model) {
        return mealService.getEditMealPage(id, model);
    }

    @GetMapping(value = "/details/{id}")
    public Mono<String> getMealDetailsPage(@PathVariable Long id, Model model) {
        return mealService.getMealDetailsPage(id, model);
    }

    @PostMapping(value = "/addOrUpdateMeal")
    @ResponseBody
    public Mono<WebPageResponse> addOrUpdateMeal(@RequestBody AddMealRequestDTO addMealRequestDTO) {
        return mealService.addOrUpdateMeal(addMealRequestDTO);
    }
    @GetMapping("/search")
    @ResponseBody
    public Mono<List<MealMainInfoDTO>> searchIngredients(@RequestParam("query") String query) {
        return mealService.searchMealsByName(query);
    }
}
