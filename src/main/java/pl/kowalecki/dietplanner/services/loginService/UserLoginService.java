package pl.kowalecki.dietplanner.services.loginService;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pl.kowalecki.dietplanner.model.DTO.User.LoginRequestDTO;
import reactor.core.publisher.Mono;

import static pl.kowalecki.dietplanner.utils.UrlTools.AUTH_SERVICE_URL;

@Service
public class UserLoginService {

    private final WebClient webClient;

    public UserLoginService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(AUTH_SERVICE_URL).build();
    }

    public Mono<ResponseEntity<Object>> postUserLogin(LoginRequestDTO loginRequestDto) {
        return webClient.post()
                .uri("/login")
                .bodyValue(loginRequestDto)
                .retrieve()
                .toEntity(Object.class);
    }
}
