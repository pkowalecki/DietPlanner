package pl.kowalecki.dietplanner.services.dietplannerapi.meal;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pl.kowalecki.dietplanner.model.DTO.FoodBoardPageRequest;
import pl.kowalecki.dietplanner.model.DTO.MealStarterPack;
import pl.kowalecki.dietplanner.model.DTO.PageResponse;
import pl.kowalecki.dietplanner.model.DTO.meal.AddMealRequestDTO;
import pl.kowalecki.dietplanner.model.DTO.meal.MealBoardDTO;
import pl.kowalecki.dietplanner.model.DTO.meal.MealMainInfoDTO;
import pl.kowalecki.dietplanner.model.Meal;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

import static pl.kowalecki.dietplanner.utils.UrlTools.MEAL_SERVICE_URL;

@Service
public class DietPlannerApiMealService {

    private final WebClient webClient;

    public DietPlannerApiMealService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<MealStarterPack> getMealStarterPack() {
        return webClient.get()
                .uri(MEAL_SERVICE_URL+"/meal/getMealStarterPack")
                .retrieve()
                .bodyToMono(MealStarterPack.class);
    }

    public Mono<List<Meal>> getAllMeals() {
        return webClient.get()
                .uri(MEAL_SERVICE_URL+"/meal/allMeal")
                .retrieve()
                .bodyToFlux(Meal.class)
                .collectList();
    }

    public Mono<MealBoardDTO> generateMealBoard(FoodBoardPageRequest apiReq) {
        return webClient.post()
                .uri(MEAL_SERVICE_URL+"/meal/generateFoodBoard")
                .bodyValue(apiReq)
                .retrieve()
                .bodyToMono(MealBoardDTO.class);
    }

    public Mono<ResponseEntity<Void>> addMeal(AddMealRequestDTO addMealRequestDTO) {
        return webClient.post()
                .uri(MEAL_SERVICE_URL+"/meal/addMeal")
                .bodyValue(addMealRequestDTO)
                .retrieve()
                .toBodilessEntity();
    }

    public Mono<PageResponse<MealMainInfoDTO>> getPageMeals(int page, int size, String mealType) {
        String url = MEAL_SERVICE_URL + "/meal/getMealsData?page=" + page + "&size=" + size + "&mealType=" + mealType;

        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PageResponse<MealMainInfoDTO>>() {})
                .onErrorReturn(new PageResponse<>(Collections.emptyList(), page, size, 0, 0));
    }
}
