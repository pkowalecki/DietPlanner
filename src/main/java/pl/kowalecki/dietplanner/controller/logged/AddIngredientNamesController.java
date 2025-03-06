package pl.kowalecki.dietplanner.controller.logged;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import pl.kowalecki.dietplanner.UrlBuilder;
import pl.kowalecki.dietplanner.client.dpa.ingredient.DietPlannerApiIngredientClient;
import pl.kowalecki.dietplanner.controller.helper.IngredientNamesHelper;
import pl.kowalecki.dietplanner.model.ClientErrorResponse;
import pl.kowalecki.dietplanner.model.WebPageResponse;
import pl.kowalecki.dietplanner.model.ingredient.IngredientName;
import pl.kowalecki.dietplanner.service.WebPage.IWebPageResponseBuilder;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
@RequestMapping("/app/auth")
public class AddIngredientNamesController {

    private final String ADD_INGREDIENT_VIEW = "pages/logged/addIngredient";

    private final DietPlannerApiIngredientClient apiClient;
    private final IngredientNamesHelper ingredientNamesHelper;
    private final IWebPageResponseBuilder webPageResponse;

    @GetMapping(value = "/addIngredient")
    public String addIngredientPage(Model model) {
        UrlBuilder builder = new UrlBuilder("/app/auth/ingredientNames/search");
        model.addAttribute("liveSearchUrl", builder.buildUrl());
        return ADD_INGREDIENT_VIEW;
    }

    //TODO na webie poprawić wymagane pola
    @PostMapping(value = "/addIngredientName")
    @ResponseBody
    public Mono<WebPageResponse> addIngredient(@RequestBody IngredientName ingredientName) {
        Map<String, String> errors = ingredientNamesHelper.checkIngredients(ingredientName);
        if (!errors.isEmpty()) {
            return Mono.just(webPageResponse.buildErrorWithFields(errors));
        }
        return apiClient.addIngredientName(ingredientName)
                .flatMap(response -> {
                    if (response.getStatusCode().is2xxSuccessful()) {
                        return Mono.just(webPageResponse.buildRedirect("/app/auth/addIngredient", "Składnik został dodany."));
                    } else {
                        return Mono.just(webPageResponse.buildErrorMessage("Nie udało się dodać składnika."));
                    }
                })
                .onErrorResume(WebClientResponseException.class, ex -> {
                    try {
                        ClientErrorResponse errorResponse = new ObjectMapper()
                                .readValue(ex.getResponseBodyAsString(), ClientErrorResponse.class);
                        return Mono.just(webPageResponse.buildErrorMessage(errorResponse.getMessage()));
                    } catch (Exception parseEx) {
                        return Mono.just(webPageResponse.buildErrorMessage("Wystąpił nieoczekiwany błąd"));
                    }
                });
    }

    @GetMapping("/ingredientNames/search")
    @ResponseBody
    public Mono<List<IngredientName>> searchIngredients(@RequestParam("query") String query) {
        return apiClient.searchIngredientName(query);
    }

}
