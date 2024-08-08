package pl.kowalecki.dietplanner.controller.unlogged;

import jakarta.servlet.http.HttpSession;

import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import pl.kowalecki.dietplanner.model.DTO.ResponseDTO;
import pl.kowalecki.dietplanner.model.DTO.LoginRequestDTO;
import pl.kowalecki.dietplanner.services.RestClientService;
import pl.kowalecki.dietplanner.utils.UrlTools;


@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RequestMapping("/app")
@Controller
@AllArgsConstructor
public class LoginController {


    private final RestClientService restClientService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(){
        return "pages/unlogged/index";
    }

    @PostMapping("/login")
    public String postLoginPage(@ModelAttribute("loginForm") LoginRequestDTO loginRequestDto, HttpSession session, Model model){
        try {
            String url = "http://"+UrlTools.apiUrl +"/login";
            ResponseEntity<ResponseDTO> response = restClientService.sendPostRequest(url, loginRequestDto, ResponseDTO.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                ResponseDTO responseDTO = response.getBody();
                session.setAttribute("user", loginRequestDto.getEmail());
                model.addAttribute("roles", responseDTO.getData());
                return "pages/foodBoardPage";
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
