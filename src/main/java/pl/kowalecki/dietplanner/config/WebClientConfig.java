package pl.kowalecki.dietplanner.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;
import pl.kowalecki.dietplanner.exception.UnauthorizedException;
import pl.kowalecki.dietplanner.utils.CookieUtils;

@Slf4j
@Configuration
@AllArgsConstructor
public class WebClientConfig {

    private final CookieUtils cookieUtils;

    @Bean
    public WebClient webClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .filter((request, next) -> {

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
                })
                .build();
    }
}
