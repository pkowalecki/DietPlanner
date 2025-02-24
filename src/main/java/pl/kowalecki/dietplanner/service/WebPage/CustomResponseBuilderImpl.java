package pl.kowalecki.dietplanner.service.WebPage;

import org.springframework.stereotype.Component;
import pl.kowalecki.dietplanner.model.CustomResponse;

@Component
public class CustomResponseBuilderImpl implements ICustomResponseBuilder{

    @Override
    public CustomResponse buildRedirect(String url) {
        return CustomResponse.builder()
                .status(ActionType.REDIRECT)
                .redirectUrl(url)
                .build();
    }

    @Override
    public CustomResponse buildMessage(String message) {
        return CustomResponse.builder()
                .status(ActionType.MESSAGE)
                .message(message)
                .icon("success")
                .build();
    }

    @Override
    public CustomResponse buildError(String message) {
        return CustomResponse.builder()
                .status(ActionType.ERROR)
                .message(message)
                .icon("error")
                .build();
    }
}
