package pl.kowalecki.dietplanner.controller.logged;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.kowalecki.dietplanner.UrlBuilder;
import pl.kowalecki.dietplanner.controller.helper.IngredientNamesHelper;
import pl.kowalecki.dietplanner.model.ingredient.IngredientName;
import pl.kowalecki.dietplanner.services.WebPage.IWebPageService;
import pl.kowalecki.dietplanner.services.WebPage.MessageType;
import pl.kowalecki.dietplanner.services.dietplannerapi.ingredientName.DietPlannerApiIngredientNameService;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
@RequestMapping("/app/auth")
public class AddIngredientNamesController {

    private final String ADD_INGREDIENT_VIEW = "pages/logged/addIngredient";

    private final DietPlannerApiIngredientNameService apiClient;
    private final IngredientNamesHelper ingredientNamesHelper;
    private final IWebPageService webPageService;

    @GetMapping(value = "/addIngredient")
    public String addIngredientPage(Model model) {
        UrlBuilder builder = new UrlBuilder("/app/auth/ingredientNames/search");
        model.addAttribute("liveSearchUrl", builder.buildUrl());
        return ADD_INGREDIENT_VIEW;
    }

    @PostMapping(value = "/addIngredientName")
    public Mono<ResponseEntity<Map<String,String>>> addIngredient(@RequestBody IngredientName ingredientName){
        Map<String, String> errors = ingredientNamesHelper.checkIngredients(ingredientName);
        if(!errors.isEmpty()){
            return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors));
        }
        return apiClient.addIngredientName(ingredientName).flatMap(
                response -> {
                    if (response.getStatusCode().is2xxSuccessful()){
                        return Mono.just(ResponseEntity.status(HttpStatus.OK).body(webPageService.addMessageToPage(MessageType.SUCCESS, "Składnik został dodany")));
                    }else if(response.getStatusCode().is4xxClientError()) {
                        return Mono.just(ResponseEntity.status(response.getStatusCode()).body(webPageService.addMessageToPage( MessageType.ERROR, "Nie udało się dodać składnika.")));
                    }else{
                        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(webPageService.addMessageToPage(MessageType.ERROR, "Wystąpił nieoczekiwany błąd serwera")));
                    }
                })
            .onErrorResume(error -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(webPageService.addMessageToPage(MessageType.ERROR, "Wystąpił nieoczekiwany błąd serwera"))));
    }

    @GetMapping("/ingredientNames/search")
    @ResponseBody
    public Mono<List<IngredientName>> searchIngredients(@RequestParam("query") String query) {
        return apiClient.searchIngredientName(query);
    }

}
