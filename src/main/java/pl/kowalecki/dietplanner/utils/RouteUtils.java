package pl.kowalecki.dietplanner.utils;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RouteUtils {

    private static final List<String> OPEN_PATHS = List.of(
            "/",
            "/app/",
            "/app/login",
            "/register",
            "/registerModal"
    );

    /**
     * Sprawdza, czy podana ścieżka jest otwarta.
     *
     * @param requestUri Ścieżka żądania
     * @return true, jeśli ścieżka jest otwarta
     */
    public boolean isOpenPath(String requestUri) {
        return OPEN_PATHS.stream().anyMatch(requestUri::equals) || requestUri.startsWith("/static/");
    }
}
