package pl.kowalecki.dietplanner.service.meal.mealManagement;

import org.springframework.ui.Model;
import pl.kowalecki.dietplanner.model.DTO.meal.AddMealRequestDTO;
import pl.kowalecki.dietplanner.model.DTO.meal.MealMainInfoDTO;
import pl.kowalecki.dietplanner.model.WebPageResponse;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IMealManagementService {

    Mono<String> getAddMealPage(Model model);

    Mono<WebPageResponse> addOrUpdateMeal(AddMealRequestDTO addMealRequestDTO);

    Mono<String> getMealDetailsPage(Long id, Model model);

    Mono<String> getEditMealPage(Long id, Model model);

    Mono<List<MealMainInfoDTO>> searchMealsByName(String query);
}
