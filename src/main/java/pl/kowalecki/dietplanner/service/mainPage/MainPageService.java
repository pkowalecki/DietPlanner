package pl.kowalecki.dietplanner.service.mainPage;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import pl.kowalecki.dietplanner.client.dpa.meal.DietPlannerApiClient;
import pl.kowalecki.dietplanner.model.DTO.PageResponse;
import pl.kowalecki.dietplanner.model.DTO.meal.MealMainInfoDTO;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
public class MainPageService implements IMainPageService {

    private final DietPlannerApiClient dpaClient;

    @Override
    public Mono<String> getMainBoardData(String mealType, int page, Model model) {
        int currentPage = Math.max(page, 1);
        return dpaClient.getPageMeals(currentPage - 1, 10, mealType)
                .flatMap(response -> {
                    List<MealMainInfoDTO> meals = Optional.ofNullable(response)
                            .map(PageResponse::getContent)
                            .orElse(Collections.emptyList());

                    int totalPages = Optional.ofNullable(response)
                            .map(PageResponse::getTotalPages)
                            .orElse(1);

                    long totalElements = Optional.ofNullable(response)
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

                    return Mono.just("pages/logged/loggedPage");
                });
    }
}
