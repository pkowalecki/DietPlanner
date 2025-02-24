package pl.kowalecki.dietplanner.service.register;

import pl.kowalecki.dietplanner.model.CustomResponse;
import pl.kowalecki.dietplanner.model.DTO.RegistrationRequestDTO;
import reactor.core.publisher.Mono;

public interface IRegisterService {
    Mono<CustomResponse> registerUser(RegistrationRequestDTO registrationRequest);
}
