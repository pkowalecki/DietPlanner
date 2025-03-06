package pl.kowalecki.dietplanner.client.dpa.ingredient;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pl.kowalecki.dietplanner.model.ingredient.IngredientName;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static pl.kowalecki.dietplanner.utils.UrlTools.MEAL_SERVICE_URL;

@Service
@RequiredArgsConstructor
@Slf4j
public class DietPlannerApiIngredientClient {

    private final static String INGREDIENT_URL = MEAL_SERVICE_URL + "/ingredient";

    private final WebClient webClient;
    public Mono<ResponseEntity<Void>> addIngredientName(IngredientName ingredientName) {
        return webClient.post()
                .uri(INGREDIENT_URL + "/ingredientNames/addOrEditIngredientDetails")
                .bodyValue(ingredientName)
                .retrieve()
                .toBodilessEntity();
    }

    public Mono<List<IngredientName>> searchIngredientName(String query) {
        String url = INGREDIENT_URL + "/ingredientNames/search?query=" + URLEncoder.encode(query, StandardCharsets.UTF_8);
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<IngredientName>>() {});
    }

}
