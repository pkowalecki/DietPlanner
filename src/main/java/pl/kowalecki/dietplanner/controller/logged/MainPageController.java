package pl.kowalecki.dietplanner.controller.logged;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.kowalecki.dietplanner.service.mainPage.IMainPageService;
import reactor.core.publisher.Mono;


@Controller
@RequestMapping("/auth")
@AllArgsConstructor
public class MainPageController {

    IMainPageService mainPageService;

    @GetMapping("/mainBoard")
    public Mono<String> getLoggedUserBoard(
            @RequestParam(value = "mealType", required = false, defaultValue = "all") String mealType,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            Model model) {

        return mainPageService.getMainBoardData(mealType, page, model);
    }
    @PostMapping("/mainBoard")
    public Mono<String> filterDataOnPage(
            @RequestParam(value = "mealType", required = false, defaultValue = "all") String mealType,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            Model model) {

        return mainPageService.getMainBoardData(mealType, page, model);
    }
}
