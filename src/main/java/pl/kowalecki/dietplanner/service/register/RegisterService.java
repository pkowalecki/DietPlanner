package pl.kowalecki.dietplanner.service.register;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.kowalecki.dietplanner.client.auth.AuthClient;
import pl.kowalecki.dietplanner.controller.helper.RegisterHelper;
import pl.kowalecki.dietplanner.model.CustomResponse;
import pl.kowalecki.dietplanner.model.DTO.RegistrationRequestDTO;
import pl.kowalecki.dietplanner.service.WebPage.ICustomResponseBuilder;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class RegisterService implements IRegisterService {

    private final AuthClient authClient;
    private final RegisterHelper registerHelper;
    private final ICustomResponseBuilder responseBuilder;


    @Override
    public Mono<CustomResponse> registerUser(RegistrationRequestDTO registrationRequest) {
        Map<String, String> errors = registerHelper.checkRegistrationData(registrationRequest);
        if (!errors.isEmpty()) {
            return Mono.just(responseBuilder.buildError("Validation failed"));
        }

        return authClient.registerUser(registrationRequest)
                .map(httpStatus -> {
                    if (httpStatus.is2xxSuccessful()) {
                        return responseBuilder.buildMessage("User registered successfully");
                    } else {
                        return responseBuilder.buildError("Registration failed with status: " + httpStatus);
                    }
                })
                .onErrorResume(e -> {
                    log.error("Registration error: {}", e.getMessage(), e);
                    return Mono.just(responseBuilder.buildError("Registration error: " + e.getMessage()));
                });
    }
}
