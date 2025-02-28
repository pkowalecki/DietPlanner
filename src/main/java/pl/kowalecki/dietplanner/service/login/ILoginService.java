package pl.kowalecki.dietplanner.service.login;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;
import pl.kowalecki.dietplanner.model.DTO.User.LoginRequest;
import pl.kowalecki.dietplanner.model.WebPageResponse;
import reactor.core.publisher.Mono;

public interface ILoginService {

    Mono<WebPageResponse> login(LoginRequest loginRequest, Model model, HttpServletResponse httpResponse);
}
