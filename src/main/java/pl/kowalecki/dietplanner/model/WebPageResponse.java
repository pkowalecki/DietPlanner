package pl.kowalecki.dietplanner.model;

import lombok.Builder;
import lombok.Data;
import pl.kowalecki.dietplanner.service.WebPage.ActionType;

import java.util.Map;

@Data
@Builder
public class WebPageResponse {
    private ActionType status;
    private String message;
    private String icon;
    private String redirectUrl;
    private Map<String, String> additionalData;
}
