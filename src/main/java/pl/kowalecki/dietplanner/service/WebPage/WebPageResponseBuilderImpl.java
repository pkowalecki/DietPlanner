package pl.kowalecki.dietplanner.service.WebPage;

import org.springframework.stereotype.Component;
import pl.kowalecki.dietplanner.model.WebPageResponse;

import java.util.Map;

@Component
public class WebPageResponseBuilderImpl implements IWebPageResponseBuilder {

    @Override
    public WebPageResponse buildRedirect(String url){
        return createRedirect(url, "");
    }
    @Override
    public WebPageResponse buildRedirect(String url, String message){
        return createRedirect(url, message);
    }

    @Override
    public WebPageResponse buildMessage(String message, boolean success) {
        return WebPageResponse.builder()
                .status(success ? ActionType.SUCCESS : ActionType.MESSAGE)
                .message(message)
                .icon(success ? "success" : "info")
                .build();
    }

    @Override
    public WebPageResponse buildErrorMessage(String message) {
        return WebPageResponse.builder()
                .status(ActionType.ERROR)
                .message(message)
                .icon("error")
                .build();
    }

    @Override
    public WebPageResponse buildErrorWithFields(Map<String, String> additionalData) {
        return WebPageResponse.builder()
                .status(ActionType.ERROR)
                .additionalData(additionalData)
                .build();
    }

    private WebPageResponse createRedirect(String url, String message) {
        if (message != null && !message.isEmpty()) {
            return WebPageResponse.builder()
                    .status(ActionType.REDIRECT)
                    .redirectUrl(url)
                    .message(message)
                    .build();
        } else {
            return WebPageResponse.builder()
                    .status(ActionType.REDIRECT)
                    .redirectUrl(url)
                    .build();
        }
    }
}
