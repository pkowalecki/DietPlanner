package pl.kowalecki.dietplanner.controller.ingredient;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.kowalecki.dietplanner.model.WebPageResponse;
import pl.kowalecki.dietplanner.model.ingredient.IngredientName;
import pl.kowalecki.dietplanner.service.ingredient.ingredientManagement.IIngredientManagementService;
import reactor.core.publisher.Mono;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/auth/ingredient")
public class IngredientManagementController {

    private final IIngredientManagementService ingredientManagementService;

    @GetMapping(value = "/")
    public Mono<String> addIngredientPage() {
        return ingredientManagementService.getAddIngredientPage();
    }

    @PostMapping(value = "/addIngredientName")
    @ResponseBody
    public Mono<WebPageResponse> addIngredient(@RequestBody IngredientName ingredientName) {
        return ingredientManagementService.addIngredient(ingredientName);
    }

    @GetMapping("/ingredientNames/search")
    @ResponseBody
    public Mono<List<IngredientName>> searchIngredients(@RequestParam("query") String query) {
        return ingredientManagementService.searchIngredients(query);
    }

}
