package pl.kowalecki.dietplanner.service.register;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import pl.kowalecki.dietplanner.client.auth.AuthClient;
import pl.kowalecki.dietplanner.controller.helper.RegisterHelper;
import pl.kowalecki.dietplanner.model.ClientErrorResponse;
import pl.kowalecki.dietplanner.model.WebPageResponse;
import pl.kowalecki.dietplanner.model.DTO.RegistrationRequestDTO;
import pl.kowalecki.dietplanner.service.WebPage.IWebPageResponseBuilder;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class RegisterService implements IRegisterService {

    private final AuthClient authClient;
    private final RegisterHelper registerHelper;
    private final IWebPageResponseBuilder responseBuilder;


    @Override
    public Mono<WebPageResponse> registerUser(RegistrationRequestDTO registrationRequest) {
        Map<String, String> errors = registerHelper.checkRegistrationData(registrationRequest);
        if (!errors.isEmpty()) {
            return Mono.just(responseBuilder.buildErrorWithFields(errors));
        }
        return authClient.registerUser(registrationRequest)
                .map(response -> responseBuilder.buildMessage("Użytkownik zarejestrowany pomyślnie", true))
                .onErrorResume(WebClientResponseException.class, ex -> {
                    try {
                        ClientErrorResponse errorResponse = new ObjectMapper()
                                .readValue(ex.getResponseBodyAsString(), ClientErrorResponse.class);
                        return Mono.just(responseBuilder.buildErrorMessage(errorResponse.getMessage()));
                    } catch (Exception parseEx) {
                        return Mono.just(responseBuilder.buildErrorMessage("Wystąpił nieoczekiwany błąd"));
                    }
                })
                .onErrorResume(e ->
                        Mono.just(responseBuilder.buildErrorMessage("Wystąpił nieoczekiwany błąd")));
    }

    @Override
    public Mono<String> confirmUser(String confirmationToken, Model model) {
        return authClient.confirmRegistration(confirmationToken)
                .flatMap(response -> {
                    if (response.getStatusCode().is2xxSuccessful()) {
                        model.addAttribute("activated", true);
                        return Mono.just("pages/unlogged/confirmation");
                    }
                    return Mono.error(new RuntimeException("Nieoczekiwany błąd klienta"));
                })
                .onErrorResume(WebClientResponseException.class, ex -> {
                    if (ex.getStatusCode().is5xxServerError()) {
                        return Mono.error(ex);
                    }
                    model.addAttribute("failed", true);
                    try {
                        ClientErrorResponse errorResponse = new ObjectMapper()
                                .readValue(ex.getResponseBodyAsString(), ClientErrorResponse.class);
                        model.addAttribute("alreadyActive", errorResponse.getMessage());
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    return Mono.just("pages/unlogged/confirmation");
                });
    }
}
