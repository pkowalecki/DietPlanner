package pl.kowalecki.dietplanner.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import pl.kowalecki.dietplanner.exception.ClientErrorException;
import pl.kowalecki.dietplanner.exception.UnauthorizedException;
import pl.kowalecki.dietplanner.model.ClientErrorResponse;
import pl.kowalecki.dietplanner.model.WebPageResponse;
import pl.kowalecki.dietplanner.service.WebPage.IWebPageResponseBuilder;
import pl.kowalecki.dietplanner.utils.CookieUtils;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@AllArgsConstructor
public class WebClientConfig {

    private final IWebPageResponseBuilder webResponse;
    private final CookieUtils cookieUtils;

    @Bean
    public WebClient webClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .filter(authorizationFilter())
                .filter(errorHandlingFilter())
                .build();
    }

    private ExchangeFilterFunction authorizationFilter() {
        return (request, next) -> {
            HttpServletRequest httpRequest =
                    ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            HttpServletResponse httpResponse =
                    ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();

            String accessToken = cookieUtils.extractJwtokenFromAccessCookie(httpRequest);
            String refreshToken = cookieUtils.extractRefreshTokenFromRefreshCookie(httpRequest);

            if (accessToken == null && refreshToken == null) {
                throw new UnauthorizedException("Authorization failed. Please log in.");
            }

            ClientRequest.Builder requestBuilder = ClientRequest.from(request);

            if (accessToken != null) {
                requestBuilder.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
            } else {
                requestBuilder.header("X-Refresh-Token", refreshToken);
            }

            return next.exchange(requestBuilder.build())
                    .doOnSuccess(clientResponse -> {
                        if (accessToken == null) {
                            String newAccessToken = clientResponse.headers()
                                    .asHttpHeaders()
                                    .getFirst(HttpHeaders.AUTHORIZATION);

                            if (newAccessToken != null) {
                                cookieUtils.setAccessTokenCookie(httpResponse, newAccessToken.replace("Bearer ", ""), 15 * 60);
                            }
                        }
                    });
        };
    }

    private ExchangeFilterFunction errorHandlingFilter() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            String timestamp = LocalDateTime.now().format(dtf);

            if (clientResponse.statusCode().equals(HttpStatus.UNAUTHORIZED)) {
                return clientResponse.bodyToMono(ClientErrorResponse.class)
                        .onErrorResume(e -> Mono.just(new ClientErrorResponse("Sesja wygasła", "Unauthorized", 401, "gateway", clientResponse.request().getURI().toString(), timestamp)))
                        .flatMap(errorBody -> {
                            log.error("[{}] Unauthorized access: {}", timestamp, errorBody.getMessage());
                            WebPageResponse response = webResponse.buildRedirect("/app/login?sessionExpired=true");
                            return Mono.error(new UnauthorizedException(response.getMessage()));
                        });
            } else if (clientResponse.statusCode().is5xxServerError()) {
                return clientResponse.bodyToMono(ClientErrorResponse.class)
                        .onErrorResume(e -> Mono.just(new ClientErrorResponse("Błąd klienta", "ClientError", clientResponse.statusCode().value(), "unknown", clientResponse.request().getURI().toString(), timestamp)))
                        .flatMap(errorBody -> {
                            log.error("[{}] Client error from {}: {} ({}). Requested URL: {}", timestamp, errorBody.getServiceName(), errorBody.getMessage(), errorBody.getStatusCode(), errorBody.getRequestedUrl());
//                        return Mono.error(webResponse.buildErrorMessage("Wystąpił błąd klienta"));
                            return Mono.error(new ClientErrorException(errorBody.getMessage(), errorBody.getStatusCode(), errorBody.getRequestedUrl()));
                        });
            }
            return Mono.just(clientResponse);
        });
    }
}
