package pl.kowalecki.dietplanner.service.meal.shoppingList;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import pl.kowalecki.dietplanner.client.dpa.meal.DietPlannerApiClient;
import pl.kowalecki.dietplanner.model.DTO.FoodBoardPageRequest;
import pl.kowalecki.dietplanner.model.WebPageResponse;
import pl.kowalecki.dietplanner.service.WebPage.IWebPageResponseBuilder;
import pl.kowalecki.dietplanner.utils.TextTools;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ShoppingListService implements IShoppingListService{

    private final DietPlannerApiClient dpaClient;
    private final IWebPageResponseBuilder responseBuilder;
    @Override
    public Mono<String> getShoppingList(String pageId, Model model) {
        return dpaClient.getShoppingList(pageId)
                .map(shoppingList -> {
                    model.addAttribute("pageId", pageId);
                    model.addAttribute("ingredients", shoppingList);
                    return "pages/logged/foodBoardResult";
                });
    }

    @Override
    public Mono<WebPageResponse> postShoppingListData(Map<String, Object> data) {
        Map<String, Map<String, Long>> meals = prepareMeals(data);

        List<Long> mealIds = prepareIds(meals);

        if (mealIds.stream().allMatch(val -> val.equals(-1L))) {
            Map<String, String> result = new HashMap<>();
            result.put("errorMsg", "Musisz wybrać jakiś posiłek");
            return Mono.just(responseBuilder.buildErrorWithFields(result));
        }
        Double multiplier = data.containsKey("multiplier")
                ? Double.valueOf(data.get("multiplier").toString()) : 1.0;
        FoodBoardPageRequest requestData = new FoodBoardPageRequest();
        requestData.setMealIds(mealIds);
        requestData.setMultiplier(multiplier);

        return dpaClient.generateMealBoard(requestData)
                .map(urlParam -> responseBuilder.buildRedirect("/app/auth/shoppingList/" + urlParam));
    }


    private Map<String, Map<String, Long>> prepareMeals(Map<String, Object> rawData) {
        Map<String, Map<String, Long>> meals = new HashMap<>();
        rawData.forEach((key, value) -> {
            if (key.startsWith("meals[")) {
                String[] parts = key.replaceAll("[^0-9]+", " ").trim().split(" ");
                if (parts.length == 2) {
                    String day = parts[0];
                    String mealType = parts[1];

                    if (TextTools.isTextLengthOk(day, 1, 7) && TextTools.isTextLengthOk(mealType, 1, 4)) {
                        meals.computeIfAbsent(day, k -> new HashMap<>())
                                .put(mealType, Long.valueOf(value.toString()));
                    }
                }
            }
        });

        return meals;
    }

    private List<Long> prepareIds(Map<String, Map<String, Long>> meals) {
        return meals.values().stream()
                .flatMap(day -> day.values().stream())
                .map(mealId -> mealId !=null ? mealId : 0L)
                .collect(Collectors.toList());

    }
}
