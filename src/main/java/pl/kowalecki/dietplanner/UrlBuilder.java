package pl.kowalecki.dietplanner;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class UrlBuilder{

    private final String path;

    public UrlBuilder(String path) {
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("Ścieżka nie może być pusta.");
        }
        this.path = path.startsWith("/") ? path : "/" + path;
    }

    public String buildUrl() {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(path)
                .toUriString();
    }

    public String getPath() {
        return path;
    }
}
