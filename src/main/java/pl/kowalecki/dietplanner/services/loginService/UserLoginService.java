package pl.kowalecki.dietplanner.services.loginService;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pl.kowalecki.dietplanner.model.DTO.User.LoginRequestDTO;
import pl.kowalecki.dietplanner.model.DTO.User.LoginResponseDTO;
import reactor.core.publisher.Mono;

@Service
public class UserLoginService {

    private final WebClient webClient;

    public UserLoginService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080/api/v1/auth").build();
    }

    public Mono<ResponseEntity<LoginResponseDTO>> postUserLogin(LoginRequestDTO loginRequestDto) {
        return webClient.post()
                .uri("/login")
                .bodyValue(loginRequestDto)
                .retrieve()
                .toEntity(LoginResponseDTO.class);
    }
}
