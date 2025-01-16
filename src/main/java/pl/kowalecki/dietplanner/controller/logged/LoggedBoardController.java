package pl.kowalecki.dietplanner.controller.logged;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.kowalecki.dietplanner.model.DTO.PageResponse;
import pl.kowalecki.dietplanner.model.DTO.meal.MealMainInfoDTO;
import pl.kowalecki.dietplanner.services.dietplannerapi.meal.DietPlannerApiMealService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/app/auth")
@AllArgsConstructor
public class LoggedBoardController {

    DietPlannerApiMealService dietPlannerApiMealService;

    @GetMapping("/loggedUserBoard")
    public String getLoggedUserBoard(
            @RequestParam(value = "mealType", required = false, defaultValue = "all") String mealType,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            Model model) {

        int currentPage = Math.max(page, 1);

        String actionType = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/app/auth/loggedUserBoard")
                .toUriString();
        model.addAttribute("actionType", actionType);

        PageResponse<MealMainInfoDTO> mealPage = dietPlannerApiMealService.getPageMeals(currentPage - 1, 10, mealType).block();

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

}
