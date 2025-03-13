package pl.kowalecki.dietplanner.service.ingredient.ingredientManagement;

import pl.kowalecki.dietplanner.model.WebPageResponse;
import pl.kowalecki.dietplanner.model.ingredient.IngredientName;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IIngredientManagementService {
    Mono<String> getAddIngredientPage();

    Mono<WebPageResponse> addIngredient(IngredientName ingredientName);

    Mono<List<IngredientName>> searchIngredients(String query);
}
