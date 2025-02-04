package pl.kowalecki.dietplanner.services.loginService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pl.kowalecki.dietplanner.model.DTO.User.LoginRequest;
import reactor.core.publisher.Mono;

import java.util.Map;

import static pl.kowalecki.dietplanner.utils.UrlTools.AUTH_SERVICE_URL;

@Service
@Slf4j
public class AuthService {

    private final WebClient webClient;

    public AuthService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(AUTH_SERVICE_URL).build();
    }

    public Mono<ResponseEntity<Map<String, String>>> postUserLogin(LoginRequest loginRequest) {
        return webClient.post()
                .uri("/login")
                .bodyValue(loginRequest)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<>() {});
    }
}
