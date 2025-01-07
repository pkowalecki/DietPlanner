package pl.kowalecki.dietplanner.services.dietplannerapi.meal;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pl.kowalecki.dietplanner.model.DTO.FoodBoardPageRequest;
import pl.kowalecki.dietplanner.model.DTO.MealStarterPack;
import pl.kowalecki.dietplanner.model.DTO.meal.AddMealRequestDTO;
import pl.kowalecki.dietplanner.model.DTO.meal.MealBoardDTO;
import pl.kowalecki.dietplanner.model.DTO.meal.MealNameDTO;
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

    public Mono<List<MealNameDTO>> getMealNamesByUserId() {
        return webClient.get()
                .uri(MEAL_SERVICE_URL+"/meal/allMeal")
                .retrieve()
                .bodyToFlux(MealNameDTO.class)
                .collectList()
                .onErrorReturn(Collections.emptyList());
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
}
