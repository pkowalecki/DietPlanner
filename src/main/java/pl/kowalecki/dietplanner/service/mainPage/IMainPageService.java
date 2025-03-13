package pl.kowalecki.dietplanner.service.mainPage;

import org.springframework.ui.Model;
import reactor.core.publisher.Mono;

public interface IMainPageService {
    Mono<String> getMainBoardData(String mealType, int page, Model model);
}
