package pl.kowalecki.dietplanner.service.WebPage;

import pl.kowalecki.dietplanner.model.WebPageResponse;

import java.util.Map;

public interface IWebPageResponseBuilder {

    WebPageResponse buildRedirect(String url);
    WebPageResponse buildRedirect(String url, String message);
    WebPageResponse buildMessage(String message, boolean success);
    WebPageResponse buildErrorMessage(String message);
    WebPageResponse buildErrorWithFields(Map<String, String> additionalData);
}
