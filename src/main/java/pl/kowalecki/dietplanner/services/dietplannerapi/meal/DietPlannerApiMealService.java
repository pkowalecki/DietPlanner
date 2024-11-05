package pl.kowalecki.dietplanner.services.dietplannerapi.meal;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pl.kowalecki.dietplanner.model.DTO.FoodBoardPageRequest;
import pl.kowalecki.dietplanner.model.DTO.ResponseBodyDTO;
import pl.kowalecki.dietplanner.model.DTO.meal.AddMealRequestDTO;
import pl.kowalecki.dietplanner.model.DTO.meal.MealBoardDTO;
import pl.kowalecki.dietplanner.model.Meal;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class DietPlannerApiMealService {

    private final WebClient webClient;

    public DietPlannerApiMealService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080/api/v1/dpa/meal").build();
    }

    public Mono<List<Meal>> getAllMeals() {
        return webClient.get()
                .uri("/meal/allMeal")
                .retrieve()
                .bodyToFlux(Meal.class)
                .collectList();
    }

    public Mono<MealBoardDTO> generateMealBoard(FoodBoardPageRequest apiReq) {
        return webClient.post()
                .uri("/meal/generateFoodBoard")
                .bodyValue(apiReq)
                .retrieve()
                .bodyToMono(MealBoardDTO.class);
    }

    public Mono<ResponseBodyDTO> addMeal(AddMealRequestDTO addMealRequestDTO){
        return webClient.post()
                .uri("/meal//addMeal")
                .bodyValue(addMealRequestDTO)
                .retrieve()
                .bodyToMono(ResponseBodyDTO.class);
    }

}
