package pl.kowalecki.dietplanner.service.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import pl.kowalecki.dietplanner.client.auth.AuthClient;
import pl.kowalecki.dietplanner.model.ClientErrorResponse;
import pl.kowalecki.dietplanner.model.DTO.User.LoginRequest;
import pl.kowalecki.dietplanner.model.WebPageResponse;
import pl.kowalecki.dietplanner.service.WebPage.IWebPageResponseBuilder;
import pl.kowalecki.dietplanner.utils.CookieUtils;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class LoginService implements ILoginService {

    private final CookieUtils cookieUtils;
    private final AuthClient loginService;
    private final IWebPageResponseBuilder responseBuilder;

    @Override
    public Mono<WebPageResponse> login(LoginRequest loginRequest, Model model, HttpServletResponse httpResponse) {
        return loginService.postUserLogin(loginRequest)
                .flatMap(loginResponse -> {
                    if (loginResponse.getStatusCode() == HttpStatus.OK && loginResponse.getBody() != null) {
                        Map<String, String> tokens = (Map<String, String>) loginResponse.getBody();
                        String accessToken = tokens.get("accessToken");
                        String refreshToken = tokens.get("refreshToken");

                        cookieUtils.setAccessTokenCookie(httpResponse, accessToken, 15 * 60);
                        cookieUtils.setRefreshTokenCookie(httpResponse, refreshToken, 7 * 24 * 60 * 60);
                        return Mono.just(responseBuilder.buildRedirect("/auth/mainBoard"));
                    } else {
                        return Mono.just(responseBuilder.buildErrorMessage("Wystąpił błąd w trakcie procesu logowania."));
                    }
                })
                .onErrorResume(WebClientResponseException.class, ex -> {
                    try {
                        ClientErrorResponse errorResponse = new ObjectMapper()
                                .readValue(ex.getResponseBodyAsString(), ClientErrorResponse.class);
                        return Mono.just(responseBuilder.buildErrorMessage(errorResponse.getMessage()));
                    } catch (Exception parseEx) {
                        return Mono.just(responseBuilder.buildErrorMessage("Wystąpił nieoczekiwany błąd"));
                    }
                });
    }

}
