package pl.kowalecki.dietplanner.services.dietplannerapi.meal;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pl.kowalecki.dietplanner.mapper.MealHistoryMapper;
import pl.kowalecki.dietplanner.model.DTO.FoodBoardPageRequest;
import pl.kowalecki.dietplanner.model.DTO.MealStarterPack;
import pl.kowalecki.dietplanner.model.DTO.meal.*;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static pl.kowalecki.dietplanner.utils.UrlTools.MEAL_SERVICE_URL;

@Service
public class DietPlannerApiMealService {

    private final WebClient webClient;
    private final MealHistoryMapper mealHistoryMapper;

    @Autowired
    public DietPlannerApiMealService(WebClient webClient, MealHistoryMapper mealHistoryMapper) {
        this.webClient = webClient;
        this.mealHistoryMapper = mealHistoryMapper;
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

    public Mono<List<MealBoardDTO>> generateMealBoard(FoodBoardPageRequest apiReq) {
        return webClient.post()
                .uri(MEAL_SERVICE_URL+"/meal/generateFoodBoard")
                .bodyValue(apiReq)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<MealBoardDTO>>() {})
                .onErrorReturn(Collections.emptyList());
    }

    public Mono<ResponseEntity<Void>> addMeal(AddMealRequestDTO addMealRequestDTO) {
        return webClient.post()
                .uri(MEAL_SERVICE_URL+"/meal/addMeal")
                .bodyValue(addMealRequestDTO)
                .retrieve()
                .toBodilessEntity();
    }

    public Mono<List<String>> getMealNamesByMealId(List<Long> mealIds){
        return webClient.post()
                .uri(MEAL_SERVICE_URL+"/meal/getMealNamesById")
                .bodyValue(mealIds)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                .onErrorReturn(Collections.emptyList());
    }

    public Mono<List<MealHistoryDTO>> getMealHistoryList(){
        return webClient.get()
                .uri(MEAL_SERVICE_URL + "/meal/getMealHistory")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<MealHistoryDTO>>() {})
                .map(list -> list.stream().map(mealHistoryMapper::mapToDto).toList())
                .onErrorReturn(Collections.emptyList());

    }

    public Mono<MealHistoryDetailsDTO> getMealHistoryById(String mealHistoryId) {
        String url = MEAL_SERVICE_URL+"/meal/getMealHistoryById?meal=" + URLEncoder.encode(mealHistoryId, StandardCharsets.UTF_8);
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(MealHistoryDetailsDTO.class)
                .onErrorReturn(new MealHistoryDetailsDTO());
    }
}
