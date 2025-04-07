package pl.kowalecki.dietplanner.controller.meal;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.kowalecki.dietplanner.model.WebPageResponse;
import pl.kowalecki.dietplanner.service.meal.shoppingList.IShoppingListService;
import reactor.core.publisher.Mono;

import java.util.Map;

@RequestMapping("/auth/shopping-list")
@Controller
@AllArgsConstructor
@Slf4j
public class ShoppingListController {

    IShoppingListService shoppingListService;

    @GetMapping(value = "/{pageId}")
    public Mono<String> getShoppingListPage(@PathVariable String pageId, Model model) {
        return shoppingListService.getShoppingList(pageId, model);
    }

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Mono<WebPageResponse> resultPageWithShoppingListAndDocument(@RequestBody Map<String, Object> rawData) {
        return shoppingListService.postShoppingListData(rawData);
    }

}
