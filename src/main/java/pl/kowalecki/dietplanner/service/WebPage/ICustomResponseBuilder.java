package pl.kowalecki.dietplanner.service.WebPage;

import pl.kowalecki.dietplanner.model.CustomResponse;

public interface ICustomResponseBuilder {
    CustomResponse buildRedirect(String url);
    CustomResponse buildMessage(String message);
    CustomResponse buildError(String message);
}
