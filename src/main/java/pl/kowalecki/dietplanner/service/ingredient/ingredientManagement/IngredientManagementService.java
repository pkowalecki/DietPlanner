package pl.kowalecki.dietplanner.service.ingredient.ingredientManagement;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.kowalecki.dietplanner.client.dpa.ingredient.DietPlannerApiIngredientClient;
import pl.kowalecki.dietplanner.controller.helper.IngredientNamesHelper;
import pl.kowalecki.dietplanner.model.WebPageResponse;
import pl.kowalecki.dietplanner.model.ingredient.IngredientName;
import pl.kowalecki.dietplanner.service.WebPage.IWebPageResponseBuilder;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class IngredientManagementService implements IIngredientManagementService{

    private final DietPlannerApiIngredientClient apiClient;
    private final IngredientNamesHelper ingredientNamesHelper;
    private final IWebPageResponseBuilder webPageResponse;

    @Override
    public Mono<String> getAddIngredientPage() {
        return Mono.just("pages/logged/addIngredient");
    }

    @Override
    public Mono<WebPageResponse> addIngredient(IngredientName ingredientName) {
        Map<String, String> errors = ingredientNamesHelper.checkIngredients(ingredientName);
        if (!errors.isEmpty()) {
            return Mono.just(webPageResponse.buildErrorWithFields(errors));
        }
        return apiClient.addIngredientName(ingredientName)
                .flatMap(response -> {
                    if (response.getStatusCode().is2xxSuccessful()) {
                        return Mono.just(webPageResponse.buildRedirect("/auth/ingredient/", "Składnik został dodany."));
                    } else {
                        return Mono.just(webPageResponse.buildErrorMessage("Nie udało się dodać składnika."));
                    }
                });
    }

    @Override
    public Mono<List<IngredientName>> searchIngredients(String query) {
        if (query.length() < 3){
            return Mono.just(Collections.emptyList());
        }
        return apiClient.searchIngredientName(query);
    }
}
