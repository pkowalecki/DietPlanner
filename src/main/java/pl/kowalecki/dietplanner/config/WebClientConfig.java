package pl.kowalecki.dietplanner.config;

import jakarta.servlet.http.HttpServletRequest;
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

    @Bean
    public WebClient webClient(WebClient.Builder webClientBuilder, CookieUtils cookieUtils) {
        return webClientBuilder
                .filter((request, next) -> {

                    HttpServletRequest httpRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

                    String accessToken = cookieUtils.extractAccessCookie(httpRequest);
                    String refreshToken = cookieUtils.extractRefreshCookie(httpRequest);

                    if (accessToken == null && refreshToken == null) {
                        log.warn("Both access and refresh tokens are missing.");
                        throw new UnauthorizedException("Authorization failed. Please log in.");
                    }
                    ClientRequest.Builder requestBuilder = ClientRequest.from(request);

                    if (accessToken != null) {
                        requestBuilder.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
                    }

                    if (refreshToken != null) {
                        requestBuilder.header("X-Refresh-Token", refreshToken);
                    }
                    log.info("headers: {}", requestBuilder.build().headers());
                    return next.exchange(requestBuilder.build());
                })
                .build();
    }

}
