package pl.kowalecki.dietplanner.services.dietplannerapi;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pl.kowalecki.dietplanner.model.DTO.MealStarterPackDTO;

import pl.kowalecki.dietplanner.model.DTO.User.LoginRequestDTO;
import pl.kowalecki.dietplanner.model.DTO.User.LoginResponseDTO;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static pl.kowalecki.dietplanner.utils.UrlTools.MEAL_SERVICE_URL;


@Service
public class DietPlannerApiService {

    private final WebClient webClient;

    public DietPlannerApiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(MEAL_SERVICE_URL).build();
    }



    public Mono<ResponseEntity<LoginResponseDTO>> postUserLogin(LoginRequestDTO loginRequestDto) {
        return webClient.post()
                .uri("/login")
                .bodyValue(loginRequestDto)
                .retrieve()
                .toEntity(LoginResponseDTO.class);
    }
}
