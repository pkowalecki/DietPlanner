package pl.kowalecki.dietplanner.client.dpa.meal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pl.kowalecki.dietplanner.mapper.MealHistoryMapper;
import pl.kowalecki.dietplanner.model.DTO.FoodBoardPageRequest;
import pl.kowalecki.dietplanner.model.DTO.MealStarterPack;
import pl.kowalecki.dietplanner.model.DTO.PageResponse;
import pl.kowalecki.dietplanner.model.DTO.meal.AddMealRequestDTO;
import pl.kowalecki.dietplanner.model.DTO.meal.MealMainInfoDTO;
import pl.kowalecki.dietplanner.model.Meal;
import pl.kowalecki.dietplanner.model.DTO.meal.*;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

import static pl.kowalecki.dietplanner.utils.UrlTools.MEAL_SERVICE_URL;

@Service
@RequiredArgsConstructor
@Slf4j
public class DietPlannerApiClient {

    private final WebClient webClient;
    private final MealHistoryMapper mealHistoryMapper;

    public Mono<MealStarterPack> getMealStarterPack() {
        return webClient.get()
                .uri(MEAL_SERVICE_URL+"/meal/getMealStarterPack")
                .retrieve()
                .bodyToMono(MealStarterPack.class);
    }

    public Mono<List<MealNameDTO>> getAllUserMeals() {
        return webClient.get()
                .uri(MEAL_SERVICE_URL+"/meal/getAllUserMeals")
                .retrieve()
                .bodyToFlux(MealNameDTO.class)
                .collectList()
                .onErrorReturn(Collections.emptyList());
    }

    public Mono<String> generateMealBoard(FoodBoardPageRequest apiReq) {
        return webClient.post()
                .uri(MEAL_SERVICE_URL+"/meal/generateFoodBoard")
                .bodyValue(apiReq)
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<List<MealBoardDTO>> getShoppingList(String pageId) {
        return webClient.post()
                .uri(MEAL_SERVICE_URL+"/meal/getShoppingListData")
                .bodyValue(pageId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<MealBoardDTO>>() {});
    }

    public Mono<ResponseEntity<Void>> addOrUpdateMeal(AddMealRequestDTO addMealRequestDTO) {
        return webClient.post()
                .uri(MEAL_SERVICE_URL+"/meal/addOrUpdateMeal")
                .bodyValue(addMealRequestDTO)
                .retrieve()
                .toBodilessEntity();
    }

    public Mono<List<String>> getMealNamesByMealId(String pageId){
        return webClient.post()
                .uri(MEAL_SERVICE_URL+"/meal/getMealNamesById")
                .bodyValue(pageId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {});
    }

    public Mono<List<MealHistoryDTO>> getMealHistoryList(){
        return webClient.get()
                .uri(MEAL_SERVICE_URL + "/meal/getMealHistory")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<MealHistoryDTO>>() {})
                .map(list -> list.stream().map(mealHistoryMapper::mapToDto).toList())
                .onErrorReturn(Collections.emptyList());

    }

    public Mono<MealHistoryDetailsDTO> getMealHistoryById(String id) {
        return webClient.post()
                .uri(MEAL_SERVICE_URL+"/meal/getMealHistoryById")
                .bodyValue(id)
                .retrieve()
                .bodyToMono(MealHistoryDetailsDTO.class);
    }


    public Mono<PageResponse<MealMainInfoDTO>> getPageMeals(int page, int size, String mealType) {
        String url = MEAL_SERVICE_URL + "/meal/getMealsData?page=" + page + "&size=" + size + "&mealType=" + mealType;

        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PageResponse<MealMainInfoDTO>>() {})
                .onErrorReturn(new PageResponse<>(Collections.emptyList(), page, size, 0, 0));
    }

    public Mono<Meal> getMealDetails(Long id) {
        return webClient.get()
                .uri(MEAL_SERVICE_URL+"/meal/getMealDetails/{id}", id)
                .retrieve()
                .bodyToMono(Meal.class);
    }

    public Mono<List<MealMainInfoDTO>> searchMealsByName(String query) {
        String url = MEAL_SERVICE_URL + "/meal/searchMealsByName?query=" + query;
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<MealMainInfoDTO>>() {})
                .doOnNext(response -> System.out.println("Response: " + response))
                .onErrorReturn(Collections.emptyList());

    }
}
