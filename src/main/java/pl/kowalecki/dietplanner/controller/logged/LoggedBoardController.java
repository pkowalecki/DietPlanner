package pl.kowalecki.dietplanner.controller.logged;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.kowalecki.dietplanner.UrlBuilder;
import pl.kowalecki.dietplanner.model.DTO.PageResponse;
import pl.kowalecki.dietplanner.model.DTO.meal.MealMainInfoDTO;
import pl.kowalecki.dietplanner.service.dietplannerapi.meal.DietPlannerApiClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/app/auth")
@AllArgsConstructor
public class LoggedBoardController {

    private final DietPlannerApiClient dietPlannerApiClient;

    @GetMapping("/loggedUserBoard")
    public String getLoggedUserBoard(
            @RequestParam(value = "mealType", required = false, defaultValue = "all") String mealType,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            Model model) {

        int currentPage = Math.max(page, 1);
        UrlBuilder actionType = new UrlBuilder("/app/auth/loggedUserBoard");
        UrlBuilder liveSearchUrl = new UrlBuilder("/app/auth/meals/search");
        UrlBuilder detailsUrl = new UrlBuilder("/app/auth/details/");
        model.addAttribute("actionType", actionType.buildUrl());
        model.addAttribute("liveSearchUrl", liveSearchUrl.buildUrl());
        model.addAttribute("details", detailsUrl.buildUrl());

        PageResponse<MealMainInfoDTO> mealPage = dietPlannerApiClient.getPageMeals(currentPage - 1, 10, mealType).block();

        List<MealMainInfoDTO> meals = Optional.ofNullable(mealPage)
                .map(PageResponse::getContent)
                .orElse(Collections.emptyList());

        int totalPages = Optional.ofNullable(mealPage)
                .map(PageResponse::getTotalPages)
                .orElse(1);

        long totalElements = Optional.ofNullable(mealPage)
                .map(PageResponse::getTotalElements)
                .orElse(0L);

        List<Integer> totalPagesListed = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());

        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPagesListed", totalPagesListed);
        model.addAttribute("meals", meals);
        model.addAttribute("activeMealType", mealType);

        return "pages/logged/loggedPage";
    }

    @GetMapping("/meals/search")
    @ResponseBody
    public Mono<List<MealMainInfoDTO>> searchIngredients(@RequestParam("query") String query) {
        if (query.length() < 3){
            return Mono.just(Collections.emptyList());
        }
        return dietPlannerApiClient.searchMealsByName(query);
    }
}
