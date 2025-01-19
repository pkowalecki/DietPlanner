package pl.kowalecki.dietplanner.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import pl.kowalecki.dietplanner.utils.AuthUtils;
import pl.kowalecki.dietplanner.utils.RouteUtils;

import java.util.List;


@Slf4j
@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(WebClient.Builder webClientBuilder, AuthUtils authUtils, RouteUtils routeUtils) {
        return webClientBuilder
                .filter((request, next) -> {
                    System.out.println("WebClientFilter");
                    HttpServletRequest httpRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

                    if (routeUtils.isOpenPath(httpRequest.getRequestURI())) {
                        log.info("Request to open path: {}", httpRequest.getRequestURI());
                        return next.exchange(request);
                    }
                    String accessToken = authUtils.extractAccessTokenFromRequest(httpRequest);
                    if (accessToken != null) {
                        log.info("Adding Authorization header to request.");
                        request = ClientRequest.from(request)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                                .build();
                    } else {
                        log.warn("No access token found in cookies for request: {}", httpRequest.getRequestURI());
                    }

                    return next.exchange(request);
                })
                .build();
    }
}
