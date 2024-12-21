package pl.kowalecki.dietplanner.services.dietplannerapi.ingredientName;

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
public class DietPlannerApiIngredientNameService {

    private final WebClient webClient;

    public DietPlannerApiIngredientNameService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<ResponseEntity<Void>> addIngredientName(IngredientName IngredientName) {
        return webClient.post()
                .uri(MEAL_SERVICE_URL + "/ingredient/ingredientNames/ingredient")
                .bodyValue(IngredientName)
                .retrieve()
                .toBodilessEntity();
    }

    public Mono<List<IngredientName>> searchIngredientName(String query) {
        String url = MEAL_SERVICE_URL + "/ingredient/ingredientNames/search?query=" + URLEncoder.encode(query, StandardCharsets.UTF_8);
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<IngredientName>>() {});
    }
}
