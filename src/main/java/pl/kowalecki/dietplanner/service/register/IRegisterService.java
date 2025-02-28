package pl.kowalecki.dietplanner.service.register;

import org.springframework.ui.Model;
import pl.kowalecki.dietplanner.model.WebPageResponse;
import pl.kowalecki.dietplanner.model.DTO.RegistrationRequestDTO;
import reactor.core.publisher.Mono;

public interface IRegisterService {
    Mono<WebPageResponse> registerUser(RegistrationRequestDTO registrationRequest);

    Mono<String> confirmUser(String confirmationToken, Model model);
}
