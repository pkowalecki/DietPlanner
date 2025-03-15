package pl.kowalecki.dietplanner.service.meal.shoppingList;

import org.springframework.ui.Model;
import pl.kowalecki.dietplanner.model.WebPageResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface IShoppingListService {

    Mono<String> getShoppingList(String pageId, Model model);

    Mono<WebPageResponse> postShoppingListData(Map<String, Object> data);
}
