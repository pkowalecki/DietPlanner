package pl.kowalecki.dietplanner.controller.unlogged;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import pl.kowalecki.dietplanner.services.WebPage.IWebPageService;
import pl.kowalecki.dietplanner.model.DTO.ResponseBodyDTO;
import pl.kowalecki.dietplanner.model.DTO.User.LoginRequestDTO;
import pl.kowalecki.dietplanner.utils.UrlTools;

import java.util.List;


@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RequestMapping("/app")
@Controller
@AllArgsConstructor
public class LoginController{
    private final IWebPageService webPageService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(@RequestParam(value = "sessionExpired", required = false) String sessionExpired, Model model) {
        if ("true".equals(sessionExpired)) {
            model.addAttribute("sessionExpired", true);
        }
        return "pages/unlogged/index";
    }

    @PostMapping("/login")
    public String postLoginPage(@ModelAttribute("loginForm") LoginRequestDTO loginRequestDto, HttpSession session, Model model, HttpServletRequest request, HttpServletResponse httpResponse) {
        try {
            String url = "http://" + UrlTools.apiUrl + "/login";
            ResponseEntity<ResponseBodyDTO> apiResponse = webPageService.sendPostRequest(url, loginRequestDto, ResponseBodyDTO.class, request, httpResponse);
            if (apiResponse.getStatusCode() == HttpStatus.OK) {
                ResponseBodyDTO responseBodyDTO = apiResponse.getBody();
                List<String> cookies = apiResponse.getHeaders().get(HttpHeaders.SET_COOKIE);
                if (cookies != null) {
                    for (String cookieHeader : cookies) {
                        httpResponse.addHeader(HttpHeaders.SET_COOKIE, cookieHeader);
                    }
                }
                webPageService.addCommonWebData(model);
                return "redirect:/app/auth/loggedUserBoard";
            } else {
                model.addAttribute("error", "Invalid email or password");
                return "pages/unlogged/index";
            }

        } catch (AuthenticationException e) {
            System.out.println("Próbowałem się zalogować: " + loginRequestDto.getEmail());
            model.addAttribute("error", "Bad data, try again");
            return "pages/unlogged/index";
        }

    }

}
