package pl.kowalecki.dietplanner.services.dietplannerapi.meal;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pl.kowalecki.dietplanner.model.DTO.FoodBoardPageRequest;
import pl.kowalecki.dietplanner.model.DTO.MealStarterPackDTO;
import pl.kowalecki.dietplanner.model.DTO.ResponseBodyDTO;
import pl.kowalecki.dietplanner.model.DTO.meal.AddMealRequestDTO;
import pl.kowalecki.dietplanner.model.DTO.meal.MealBoardDTO;
import pl.kowalecki.dietplanner.model.Meal;
import reactor.core.publisher.Mono;

import java.util.List;

import static pl.kowalecki.dietplanner.utils.UrlTools.MEAL_SERVICE_URL;

@Service
public class DietPlannerApiMealService {

    private final WebClient webClient;

    public DietPlannerApiMealService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(MEAL_SERVICE_URL).build();
    }

    public Mono<MealStarterPackDTO> getMealStarterPack(){
        return webClient.get()
                .uri("/getMealStarterPack")
                .retrieve()
                .bodyToMono(MealStarterPackDTO.class);
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
