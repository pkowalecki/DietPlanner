package pl.kowalecki.dietplanner.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
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

        return (request, next) -> next.exchange(request)
                .flatMap(clientResponse -> {
                    if (clientResponse.statusCode().isError()) {
                        return clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    String timestamp = LocalDateTime.now().format(dtf);
                                    ClientErrorResponse errorResponse = parseErrorResponse(clientResponse, errorBody, timestamp);

                                    log.error("[{}] Client error from {}: {} ({}). Requested URL: {}",
                                            timestamp,
                                            errorResponse.getServiceName(),
                                            errorResponse.getMessage(),
                                            errorResponse.getStatusCode(),
                                            errorResponse.getRequestedUrl());

                                    return Mono.error(new ClientErrorException(
                                            errorResponse.getMessage(),
                                            errorResponse.getStatusCode(),
                                            errorResponse.getRequestedUrl()
                                    ));
                                })
                                .switchIfEmpty(Mono.defer(() -> {
                                    String timestamp = LocalDateTime.now().format(dtf);
                                    ClientErrorResponse fallbackError = createFallbackErrorResponse(clientResponse, timestamp);

                                    log.error("[{}] No error body received. Fallback error from {}: {} ({}). Requested URL: {}",
                                            timestamp,
                                            fallbackError.getServiceName(),
                                            fallbackError.getMessage(),
                                            fallbackError.getStatusCode(),
                                            fallbackError.getRequestedUrl());

                                    throw new ClientErrorException(
                                            fallbackError.getMessage(),
                                            fallbackError.getStatusCode(),
                                            fallbackError.getRequestedUrl()
                                    );
                                }))
                                .then(Mono.just(clientResponse));
                    }

                    return Mono.just(clientResponse);
                });
    }

    private ClientErrorResponse parseErrorResponse(ClientResponse clientResponse, String errorBody, String timestamp) {
        try {
            if (StringUtils.hasText(errorBody)) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode errorNode = mapper.readTree(errorBody);

                String message = extractErrorMessage(errorNode);
                String serviceName = extractServiceName(errorNode);

                return new ClientErrorResponse(
                        message,
                        "ClientError",
                        clientResponse.statusCode().value(),
                        serviceName,
                        clientResponse.request().getURI().toString(),
                        timestamp
                );
            }
        } catch (Exception e) {
            log.warn("Failed to parse error body: {}", e.getMessage());
        }

        return createFallbackErrorResponse(clientResponse, timestamp);
    }

    private String extractErrorMessage(JsonNode errorNode) {
        String[] possibleMessageFields = {"message", "error", "errorMessage", "detail"};

        for (String field : possibleMessageFields) {
            if (errorNode.has(field)) {
                String message = errorNode.get(field).asText();
                if (StringUtils.hasText(message)) {
                    return message;
                }
            }
        }

        return "Wystąpił błąd podczas przetwarzania żądania";
    }

    private String extractServiceName(JsonNode errorNode) {
        if (errorNode.has("service")) {
            return errorNode.get("service").asText("unknown");
        }
        return "unknown";
    }

    private ClientErrorResponse createFallbackErrorResponse(ClientResponse clientResponse, String timestamp) {
        return new ClientErrorResponse(
                "Wystąpił nieoczekiwany błąd",
                "ClientError",
                clientResponse.statusCode().value(),
                "unknown",
                clientResponse.request().getURI().toString(),
                timestamp
        );
    }
}
